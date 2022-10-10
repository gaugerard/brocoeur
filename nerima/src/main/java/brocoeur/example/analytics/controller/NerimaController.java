package brocoeur.example.controller;

import brocoeur.example.service.GamePlay;
import brocoeur.example.service.GameStrategy;
import brocoeur.example.service.GameStrategyTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class NerimaController {

    @PostMapping("/play")
    public ResponseEntity<ServiceRequest> postPlay(@RequestBody final ServiceRequest serviceRequest) {
        final String userId = serviceRequest.userId();
        final GameStrategyTypes gameStrategyTypes = serviceRequest.gameStrategyTypes();
        final GameStrategy gameStrategy = gameStrategyTypes.getGameStrategy();

        final GamePlay gamePlay = gameStrategy.play();
        System.out.println(userId + " used strategy: " + gameStrategy + " and will play: " + gamePlay);
        return new ResponseEntity<>(serviceRequest, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<ServiceRequest> getRandomCoin() {
        final ServiceRequest serviceRequest = new ServiceRequest("4", GameStrategyTypes.COIN_TOSS_RANDOM);
        return new ResponseEntity<>(serviceRequest, HttpStatus.OK);
    }

}
