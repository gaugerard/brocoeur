package game;

import brocoeur.example.common.request.AnalyticServiceRequest;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.games.Main;
import brocoeur.example.games.controller.GamesController;
import org.junit.jupiter.api.*;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static brocoeur.example.common.AnalyticServiceRequestTypes.MONEY_MANAGEMENT;
import static brocoeur.example.common.GameStrategyTypes.OFFLINE_COIN_TOSS_RANDOM;
import static brocoeur.example.common.GameStrategyTypes.ROULETTE_RISKY;
import static brocoeur.example.common.ServiceRequestTypes.SINGLE_PLAYER;
import static org.awaitility.Awaitility.await;

@EnableRabbit
@SpringBootTest(classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("dev")
class GameIT {

    @Autowired
    private GamesController gamesController;
    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;


    @BeforeAll
    void beforeAll() {
        final Queue testQueue = new Queue("MyQ1", false, false, true);
        final TopicExchange testTopicExchange = new TopicExchange("myexchange1");
        final Binding testBinding = BindingBuilder.bind(testQueue).to(testTopicExchange).with("MyQ1");

        rabbitAdmin.declareQueue(testQueue);
        rabbitAdmin.declareExchange(testTopicExchange);
        rabbitAdmin.declareBinding(testBinding);

        final Queue testQueue2 = new Queue("analyticInput", false, false, true);
        final TopicExchange testTopicExchange2 = new TopicExchange("analyticDirectExchange");
        final Binding testBinding2 = BindingBuilder.bind(testQueue2).to(testTopicExchange2).with("analyticInput");

        rabbitAdmin.declareQueue(testQueue2);
        rabbitAdmin.declareExchange(testTopicExchange2);
        rabbitAdmin.declareBinding(testBinding2);

        rabbitAdmin.setAutoStartup(true);
    }

    @AfterEach
    void afterEach() {
        rabbitAdmin.purgeQueue("MyQ1");
        rabbitAdmin.purgeQueue("analyticInput");
    }

    @AfterAll
    void afterAll() {
        rabbitAdmin.deleteQueue("MyQ1");
        rabbitAdmin.deleteQueue("analyticInput");
        rabbitAdmin.deleteExchange("myexchange1");
        rabbitAdmin.deleteExchange("analyticDirectExchange");
    }

    @Test
    void shouldFetchDirectServiceRequestFromMyQ1AndSendToMyAnalyticInputQueue() throws InterruptedException {
        startRabbitListener();

        var userId = "8";
        var playerRequest = new PlayerRequest(userId, ROULETTE_RISKY, 5, 354561);
        var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, 1);
        rabbitAdmin.getRabbitTemplate().convertAndSend("myexchange1", "MyQ1", serviceRequest);

        await().atMost(2, TimeUnit.SECONDS).until(messageIsProcessedAndSentToQueue());

        var analyticServiceRequestPresentInQueue = (AnalyticServiceRequest) rabbitAdmin.getRabbitTemplate().receiveAndConvert("analyticInput");
        var playerResponse = analyticServiceRequestPresentInQueue.getPlayerResponseList().get(0);

        Assertions.assertEquals(MONEY_MANAGEMENT, analyticServiceRequestPresentInQueue.getAnalyticServiceRequestTypes());
        Assertions.assertEquals(123, playerResponse.getGameId());
        Assertions.assertEquals(8, playerResponse.getUserId());
        Assertions.assertEquals(1, playerResponse.getInitialAmount());
        Assertions.assertEquals(5, playerResponse.getFinalAmount());
        Assertions.assertEquals(354561, playerResponse.getLinkedJobId());
    }

    @Test
    void shouldFetchOfflineServiceRequestFromMyQ1AndSendToMyAnalyticInputQueue() throws InterruptedException {
        startRabbitListener();

        var playerRequest = new PlayerRequest("8", OFFLINE_COIN_TOSS_RANDOM, 50, 156478);
        var offlineServiceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, 3);

        rabbitAdmin.getRabbitTemplate().convertAndSend("myexchange1", "MyQ1", offlineServiceRequest);

        await().atMost(2, TimeUnit.SECONDS).until(messageIsProcessedAndSentToQueue());

        var analyticServiceRequestPresentInQueue = (AnalyticServiceRequest) rabbitAdmin.getRabbitTemplate().receiveAndConvert("analyticInput");
        var playerResponse = analyticServiceRequestPresentInQueue.getPlayerResponseList().get(0);

        Assertions.assertEquals(MONEY_MANAGEMENT, analyticServiceRequestPresentInQueue.getAnalyticServiceRequestTypes());
        Assertions.assertEquals(324, playerResponse.getGameId());
        Assertions.assertEquals(8, playerResponse.getUserId());
        Assertions.assertEquals(50, playerResponse.getInitialAmount());
        Assertions.assertEquals(50, playerResponse.getFinalAmount());
        Assertions.assertEquals(156478, playerResponse.getLinkedJobId());
    }

    private void startRabbitListener() {
        rabbitListenerEndpointRegistry.getListenerContainer(
                "game-controller-lister-id"
        ).start();
    }

    private Callable<Boolean> messageIsProcessedAndSentToQueue() {
        return new Callable<Boolean>() {
            public Boolean call() {
                // check the condition that must be fulfilled.
                var queueInfo = rabbitAdmin.getQueueInfo("analyticInput");
                return queueInfo.getMessageCount() == 1;
            }
        };
    }
}
