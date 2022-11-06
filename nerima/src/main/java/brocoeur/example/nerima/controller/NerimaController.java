package brocoeur.example.nerima.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class NerimaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NerimaController.class);
    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/api/nerima/play")
    public ResponseEntity<ServiceRequest> postPlay(@RequestBody final ServiceRequest serviceRequest) {

        LOGGER.info("serviceRequest: " + serviceRequest);

        rabbitTemplate.convertSendAndReceive("myexchange1", "", serviceRequest);

        return new ResponseEntity<>(serviceRequest, HttpStatus.OK);
    }
}
