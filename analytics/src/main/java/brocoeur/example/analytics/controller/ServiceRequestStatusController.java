package brocoeur.example.analytics.controller;

import brocoeur.example.analytics.model.ServiceRequestStatus;
import brocoeur.example.analytics.service.ServiceRequestStatusService;
import brocoeur.example.common.request.ServiceRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @GetMapping("/list")
    public Flux<ServiceRequestStatus> getAllServiceRequestStatus() {
        Flux<ServiceRequestStatus> serviceRequestStatusFlux = serviceRequestStatusService.getAllServiceRequestStatus();
        System.out.println(serviceRequestStatusFlux);
        return serviceRequestStatusFlux;
    }

    @GetMapping("/{id}")
    public Mono<ServiceRequestStatus> getServiceRequestStatusById(@PathVariable int id) {
        return serviceRequestStatusService.getServiceRequestStatusById(id);
    }

    @RabbitListener(queues = "MyA1")
    public void getMsg(final ServiceRequest serviceRequest) {
        serviceRequestStatusService.addServiceRequestStatus(serviceRequest);
    }
}