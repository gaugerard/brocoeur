package brocoeur.example.nerima.controller;

import brocoeur.example.broker.common.request.ServiceRequest;
import brocoeur.example.broker.common.response.ServiceResponse;
import brocoeur.example.nerima.NerimaConfigProperties;
import brocoeur.example.broker.common.ServiceRequestTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static brocoeur.example.broker.common.ServiceRequestTypes.DIRECT;
import static brocoeur.example.broker.common.ServiceRequestTypes.OFFLINE;

@RestController
public class NerimaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NerimaController.class);
    private static final int MAXIMUM_ALLOWED_TTL = 5;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private NerimaConfigProperties nerimaConfigProperties;

    @PostMapping("/api/nerima/play")
    public ResponseEntity<ServiceResponse> postDirectPlay(@RequestBody final ServiceRequest directServiceRequest) {

        directServiceRequest.setServiceRequestTypes(DIRECT);

        final ServiceResponse directServiceResponse = post(directServiceRequest);

        return new ResponseEntity<>(directServiceResponse, HttpStatus.OK);
    }

    @PostMapping("/api/nerima/offline/play")
    public ResponseEntity<ServiceResponse> postOfflinePlay(@RequestBody final ServiceRequest offlineServiceRequest) {

        offlineServiceRequest.setServiceRequestTypes(OFFLINE);
        offlineServiceRequest.setTimeToLive(Integer.min(offlineServiceRequest.getTimeToLive(), MAXIMUM_ALLOWED_TTL));

        final ServiceResponse offlineServiceResponse = post(offlineServiceRequest);

        return new ResponseEntity<>(offlineServiceResponse, HttpStatus.OK);
    }

    private ServiceResponse post(final ServiceRequest serviceRequest) {
        final ServiceRequestTypes serviceRequestTypes = serviceRequest.getServiceRequestTypes();

        LOGGER.info("==> " + serviceRequestTypes + " REQUEST: " + serviceRequest);
        ServiceResponse serviceResponse = (ServiceResponse) rabbitTemplate.convertSendAndReceive(
                nerimaConfigProperties.getRpcExchange(),
                nerimaConfigProperties.getRpcMessageQueue(),
                serviceRequest);
        LOGGER.info("==> " + serviceRequestTypes + " RESPONSE: " + serviceResponse);

        return serviceResponse;
    }
}
