package brocoeur.example.analytics.service;

import brocoeur.example.analytics.controller.AnalyticServiceRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CustomMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000L)
    public void sendMessage() {
        final Random rd = new Random();
        final AnalyticServiceRequest analyticServiceRequest = new AnalyticServiceRequest(324, 5, rd.nextBoolean());
        rabbitTemplate.convertAndSend("analyticDirectExchange", "analyticInput", analyticServiceRequest);
    }
}
