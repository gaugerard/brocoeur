package nerima;

import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.nerima.Main;
import brocoeur.example.nerima.controller.NerimaController;
import org.junit.jupiter.api.*;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static brocoeur.example.common.GameStrategyTypes.OFFLINE_COIN_TOSS_RANDOM;
import static brocoeur.example.common.GameStrategyTypes.ROULETTE_RISKY;
import static brocoeur.example.common.ServiceRequestTypes.SINGLE_PLAYER;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("dev")
class NerimaIT {

    @Autowired
    private NerimaController nerimaController;
    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeAll
    void beforeAll() {
        final Queue testQueue = new Queue("MyA1", false, false, true);
        final TopicExchange testTopicExchange = new TopicExchange("A1DirectExchange");
        final Binding testBinding = BindingBuilder.bind(testQueue).to(testTopicExchange).with("MyA1");
        rabbitAdmin.declareQueue(testQueue);
        rabbitAdmin.declareExchange(testTopicExchange);
        rabbitAdmin.declareBinding(testBinding);
    }

    @AfterEach
    void afterEach() {
        rabbitAdmin.purgeQueue("MyA1");
    }

    @AfterAll
    void afterAll() {
        rabbitAdmin.deleteQueue("MyA1");
        rabbitAdmin.deleteExchange("A1DirectExchange");
    }

    @Test
    void shouldSendDirectRequestToMyA1Queue() throws InterruptedException {
        var userId = "8";
        var playerRequest = new PlayerRequest(userId, ROULETTE_RISKY, 5, null);
        var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, 1);

        nerimaController.postSinglePlayerGamblePlay(serviceRequest);
        await().atMost(2, TimeUnit.SECONDS).until(messageIsProcessedAndSentToQueue());


        var serviceRequestPresentInQueue = rabbitAdmin.getRabbitTemplate().receiveAndConvert("MyA1");

        var expectedPlayerRequest = new PlayerRequest(userId, ROULETTE_RISKY, 5, null);
        var expectedServiceRequest = new ServiceRequest(SINGLE_PLAYER, expectedPlayerRequest, 1);
        Assertions.assertEquals(expectedServiceRequest, serviceRequestPresentInQueue);
    }

    @Test
    void shouldSendOfflineRequestToMyA1Queue() throws InterruptedException {
        var userId = "8";
        var playerRequest = new PlayerRequest(userId, OFFLINE_COIN_TOSS_RANDOM, 100, null);
        var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, 5);

        nerimaController.postSinglePlayerGamblePlay(serviceRequest);
        await().atMost(2, TimeUnit.SECONDS).until(messageIsProcessedAndSentToQueue());

        var serviceRequestPresentInQueue = rabbitAdmin.getRabbitTemplate().receiveAndConvert("MyA1");

        var expectedPlayerRequest = new PlayerRequest(userId, OFFLINE_COIN_TOSS_RANDOM, 100, null);
        var expectedServiceRequest = new ServiceRequest(SINGLE_PLAYER, expectedPlayerRequest, 5);
        Assertions.assertEquals(expectedServiceRequest, serviceRequestPresentInQueue);
    }

    private Callable<Boolean> messageIsProcessedAndSentToQueue() {
        return new Callable<Boolean>() {
            public Boolean call() {
                // check the condition that must be fulfilled.
                var queueInfo = rabbitAdmin.getQueueInfo("MyA1");
                return queueInfo.getMessageCount() == 1;
            }
        };
    }
}
