package brocoeur.example.nerima.controller;

import brocoeur.example.nerima.service.GamePlay;
import brocoeur.example.nerima.service.GameStrategy;
import brocoeur.example.nerima.service.GameStrategyTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class NerimaController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/api/nerima/play")
    public ResponseEntity<ServiceRequest> postPlay(@RequestBody final ServiceRequest serviceRequest) {
        final String userId = serviceRequest.userId();
        final GameStrategyTypes gameStrategyTypes = serviceRequest.gameStrategyTypes();
        final GameStrategy gameStrategy = gameStrategyTypes.getGameStrategy();
        final GamePlay gamePlay = gameStrategy.getStrategyPlay();
        System.out.println(userId + " used strategy: " + gameStrategy + " and will play: " + gamePlay);

        rabbitTemplate.send("myexchange1", "", new Message(serviceRequest));
        return new ResponseEntity<>(serviceRequest, HttpStatus.OK);
    }

    /*@GetMapping("/")
    public ResponseEntity<ServiceRequest> getRandomCoin() {
        final ServiceRequest serviceRequest = new ServiceRequest("4", GameStrategyTypes.COIN_TOSS_RANDOM);
        return new ResponseEntity<>(serviceRequest, HttpStatus.OK);
    }*/

}
