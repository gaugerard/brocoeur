package analytics;

import brocoeur.example.analytics.Main;
import brocoeur.example.analytics.controller.ServiceRequestStatusController;
import brocoeur.example.analytics.model.User;
import brocoeur.example.analytics.model.UserMoney;
import brocoeur.example.analytics.model.UserWinLossByGame;
import brocoeur.example.analytics.repository.ServiceRequestStatusRepository;
import brocoeur.example.analytics.repository.UserMoneyRepository;
import brocoeur.example.analytics.repository.UserRepository;
import brocoeur.example.analytics.repository.UserWinLossByGameRepository;
import brocoeur.example.common.request.AnalyticServiceRequest;
import brocoeur.example.common.request.ServiceRequest;
import org.junit.jupiter.api.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static brocoeur.example.common.AnalyticServiceRequestTypes.MONEY_MANAGEMENT;
import static brocoeur.example.common.GameStrategyTypes.ROULETTE_RISKY;
import static org.awaitility.Awaitility.await;

@Disabled("Not supported for the moment 12-12-22")
@EnableRabbit
@SpringBootTest(classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("dev")
class AnalyticsIT {

    int jobIdForIT;
    @Autowired
    private ServiceRequestStatusController serviceRequestStatusController;
    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
    @Autowired
    private ServiceRequestStatusRepository serviceRequestStatusRepository;
    @Autowired
    private UserMoneyRepository userMoneyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserWinLossByGameRepository userWinLossByGameRepository;

    @BeforeAll
    void beforeAll() {
        userRepository.save(new User(42, "Rambo")).subscribe();
        userMoneyRepository.save(new UserMoney(42, 1000)).subscribe();
        userWinLossByGameRepository.save(new UserWinLossByGame(123, 42, "Roulette", "Rambo", 0, 0)).subscribe();

        final Queue testQueue = new Queue("MyQ1", false, false, true);
        final TopicExchange testTopicExchange = new TopicExchange("myexchange1");
        final Binding testBinding = BindingBuilder.bind(testQueue).to(testTopicExchange).with("MyQ1");

        rabbitAdmin.declareQueue(testQueue);
        rabbitAdmin.declareExchange(testTopicExchange);
        rabbitAdmin.declareBinding(testBinding);

        final Queue testQueue2 = new Queue("analyticInput", false, false, true);
        final DirectExchange testTopicExchange2 = new DirectExchange("analyticDirectExchange", false, true);
        final Binding testBinding2 = BindingBuilder.bind(testQueue2).to(testTopicExchange2).with("analyticInput");

        rabbitAdmin.declareQueue(testQueue2);
        rabbitAdmin.declareExchange(testTopicExchange2);
        rabbitAdmin.declareBinding(testBinding2);

        final Queue testQueue3 = new Queue("MyA1", false, false, true);
        final DirectExchange testTopicExchange3 = new DirectExchange("A1DirectExchange", false, true);
        final Binding testBinding3 = BindingBuilder.bind(testQueue3).to(testTopicExchange3).with("MyA1");

        rabbitAdmin.declareQueue(testQueue3);
        rabbitAdmin.declareExchange(testTopicExchange3);
        rabbitAdmin.declareBinding(testBinding3);

        rabbitAdmin.setAutoStartup(true);
    }

    //@AfterEach
    void afterEach() {
        userMoneyRepository.save(new UserMoney(42, 1000)).subscribe();
        userWinLossByGameRepository.save(new UserWinLossByGame(123, 42, "Roulette", "Rambo", 0, 0)).subscribe();

        rabbitAdmin.purgeQueue("MyQ1");
        rabbitAdmin.purgeQueue("MyA1");
        rabbitAdmin.purgeQueue("analyticInput");
    }

    @AfterAll
    void afterAll() {
        userRepository.deleteById(42).subscribe();
        userMoneyRepository.deleteById(42).subscribe();
        var userWinLossByGameToDelete = userWinLossByGameRepository.findByGameIdAndUserId(123, 42).block();
        userWinLossByGameRepository.delete(userWinLossByGameToDelete).subscribe();
        serviceRequestStatusRepository.deleteAll().subscribe();

        rabbitAdmin.deleteQueue("MyQ1");
        rabbitAdmin.deleteQueue("MyA1");
        rabbitAdmin.deleteQueue("analyticInput");
        rabbitAdmin.deleteExchange("myexchange1");
        rabbitAdmin.deleteExchange("A1DirectExchange");
        rabbitAdmin.deleteExchange("analyticDirectExchange");
    }

    @Test
    @Order(1)
    void shouldFetchFromMyA1UpdateCassandraAndSendToMyQ1() {
        startRabbitListener();
        await().atLeast(1, TimeUnit.SECONDS);

        var userId = "42";
        var serviceRequest = new ServiceRequest(userId, ROULETTE_RISKY, 5, null);
        rabbitAdmin.getRabbitTemplate().convertAndSend("A1DirectExchange", "MyA1", serviceRequest);

        await().atMost(2, TimeUnit.SECONDS).until(messageIsProcessedAndSentToQueue("MyQ1"));

        var userMoney = userMoneyRepository.findById(42).block();
        var serviceRequestStatus = serviceRequestStatusRepository.findAll().blockFirst();

        jobIdForIT = serviceRequestStatus.getJobId();

        Assertions.assertEquals(userMoney.getUserId(), 42);
        Assertions.assertEquals(userMoney.getMoney(), 995);

        Assertions.assertEquals(serviceRequestStatus.getStatus(), "IN_PROGRESS");
        Assertions.assertEquals(serviceRequestStatus.getAmountBlocked(), 5);
        Assertions.assertEquals(serviceRequestStatus.getUserId(), 42);
        Assertions.assertEquals(serviceRequestStatus.getStrategy(), "ROULETTE_RISKY");
        Assertions.assertEquals(serviceRequestStatus.getAckTimeMilliSecond(), 0);
    }

    @Test
    @Order(2)
    void shouldFetchFromAnalyticInputUpdateCassandra() throws InterruptedException {
        var analyticServiceRequest = new AnalyticServiceRequest(MONEY_MANAGEMENT, 123, 42, true, 5, jobIdForIT);
        rabbitAdmin.getRabbitTemplate().convertAndSend("analyticDirectExchange", "analyticInput", analyticServiceRequest);

        Thread.sleep(5000);

        var userMoneyAfterGameVictory = userMoneyRepository.findById(42).block();
        var serviceRequestStatusAfterGameVictory = serviceRequestStatusRepository.findAll().blockFirst();

        Assertions.assertEquals(userMoneyAfterGameVictory.getUserId(), 42);
        Assertions.assertEquals(userMoneyAfterGameVictory.getMoney(), 1005);

        Assertions.assertEquals(serviceRequestStatusAfterGameVictory.getStatus(), "DONE_WIN");
        Assertions.assertEquals(serviceRequestStatusAfterGameVictory.getAmountBlocked(), 5);
        Assertions.assertEquals(serviceRequestStatusAfterGameVictory.getUserId(), 42);
        Assertions.assertEquals(serviceRequestStatusAfterGameVictory.getStrategy(), "ROULETTE_RISKY");
        Assertions.assertEquals(serviceRequestStatusAfterGameVictory.getJobId(), jobIdForIT);
    }

    private void startRabbitListener() {
        rabbitListenerEndpointRegistry.getListenerContainer(
                "analytics-controller-listener-id"
        ).start();
    }

    private Callable<Boolean> messageIsProcessedAndSentToQueue(final String queueName) {
        return new Callable<Boolean>() {
            public Boolean call() {
                // check the condition that must be fulfilled.
                var queueInfo = rabbitAdmin.getQueueInfo(queueName);
                return queueInfo.getMessageCount() == 1;
            }
        };
    }
}
