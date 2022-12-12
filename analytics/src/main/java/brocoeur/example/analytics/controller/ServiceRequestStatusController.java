package brocoeur.example.analytics.controller;

import brocoeur.example.analytics.service.ServiceRequestStatusService;
import brocoeur.example.common.request.ServiceRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("status")
public class ServiceRequestStatusController {

    @Autowired
    private ServiceRequestStatusService serviceRequestStatusService;

    @PostConstruct
    public void cleanup() {
        // Clean-up
        serviceRequestStatusService.deleteAllServiceRequestStatus();
    }

    @RabbitListener(id = "analytics-controller-listener-id", queues = "MyA1", autoStartup = "false")
    public void getMsg(final ServiceRequest serviceRequest) {
        serviceRequestStatusService.addServiceRequestStatus(serviceRequest);
    }
}