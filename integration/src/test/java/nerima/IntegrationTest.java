package nerima;

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

import static brocoeur.example.common.GameStrategyTypes.ROULETTE_RISKY;
import static brocoeur.example.common.OfflineGameStrategyTypes.OFFLINE_COIN_TOSS_RANDOM;

@SpringBootTest(classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("dev")
public class IntegrationTest {

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
    void afterAll(){
        rabbitAdmin.deleteQueue("MyA1");
        rabbitAdmin.deleteExchange("A1DirectExchange");
    }

    @Test
    void shouldSendDirectRequestToMyA1Queue() throws InterruptedException {
        var userId = "8";
        var serviceRequest = new ServiceRequest(userId, ROULETTE_RISKY, 5, null);

        nerimaController.postDirectGamblePlay(serviceRequest);
        Thread.sleep(2000);

        var queueInfo = rabbitAdmin.getQueueInfo("MyA1");
        var serviceRequestPresentInQueue = rabbitAdmin.getRabbitTemplate().receiveAndConvert("MyA1");

        Assertions.assertEquals(1, queueInfo.getMessageCount());
        var expectedServiceRequest = new ServiceRequest(userId, ROULETTE_RISKY, 5, null);
        Assertions.assertEquals(expectedServiceRequest, serviceRequestPresentInQueue);
    }

    @Test
    void shouldSendOfflineRequestToMyA1Queue() throws InterruptedException {
        var userId = "8";
        var serviceRequest = new ServiceRequest(userId, OFFLINE_COIN_TOSS_RANDOM, 50,  100, null);

        nerimaController.postOfflineGamblePlay(serviceRequest);
        Thread.sleep(2000);

        var queueInfo = rabbitAdmin.getQueueInfo("MyA1");
        var serviceRequestPresentInQueue = rabbitAdmin.getRabbitTemplate().receiveAndConvert("MyA1");

        Assertions.assertEquals(1, queueInfo.getMessageCount());
        var expectedServiceRequest = new ServiceRequest(userId, OFFLINE_COIN_TOSS_RANDOM, 5,  100, null);
        Assertions.assertEquals(expectedServiceRequest, serviceRequestPresentInQueue);
    }

}
