package brocoeur.example.games.controller;

import brocoeur.example.games.service.GameService;
import brocoeur.example.nerima.service.GamePlay;
import brocoeur.example.nerima.service.GameTypes;
import brocoeur.example.nerima.service.cointoss.CoinTossPlay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GamesController {

    @Autowired
    private GameService gameService;

    @GetMapping("/getGameResult")
    public ResponseEntity<GamePlay> getGameResult(@RequestParam(name = "gameTypes", required = false) GameTypes gameTypes) {
        final GamePlay gamePlay = gameService.play(gameTypes);
        return new ResponseEntity<>(gamePlay, HttpStatus.OK);
    }
}
