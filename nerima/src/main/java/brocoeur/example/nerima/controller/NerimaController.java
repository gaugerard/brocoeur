package brocoeur.example.nerima.controller;

import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.nerima.NerimaConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NerimaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NerimaController.class);
    private static final int MAXIMUM_ALLOWED_TTL = 5;

    @Autowired
    private NerimaConfigProperties nerimaConfigProperties;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/api/nerima/gamble")
    public ResponseEntity<ServiceRequest> postSinglePlayerGamblePlay(@RequestBody final ServiceRequest serviceRequest) {
        final ServiceRequest singlePlayerServiceRequest = new ServiceRequest(serviceRequest);

        singlePlayerServiceRequest.setTimeToLive(Integer.min(singlePlayerServiceRequest.getTimeToLive(), MAXIMUM_ALLOWED_TTL));

        post(singlePlayerServiceRequest);

        return new ResponseEntity<>(singlePlayerServiceRequest, HttpStatus.OK);
    }

    private void post(final ServiceRequest serviceRequest) {
        LOGGER.info("Received request : {}", serviceRequest);
        rabbitTemplate.convertAndSend(
                nerimaConfigProperties.getRpcExchange(),
                nerimaConfigProperties.getNerimaToAnalyticsQueueName(),
                serviceRequest);
    }
}
