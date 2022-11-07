package brocoeur.example.nerima.controller;

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
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private NerimaConfigProperties nerimaConfigProperties;

    @PostMapping("/api/nerima/play")
    public ResponseEntity<ServiceResponse> postPlay(@RequestBody final ServiceRequest serviceRequest) {

        LOGGER.info("==> REQUEST: " + serviceRequest);

        ServiceResponse result = (ServiceResponse) rabbitTemplate.convertSendAndReceive(nerimaConfigProperties.getRpcExchange(), nerimaConfigProperties.getRpcMessageQueue(), serviceRequest);

        LOGGER.info("==> RESPONSE: " + result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
