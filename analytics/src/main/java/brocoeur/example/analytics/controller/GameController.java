package brocoeur.example.analytics.controller;

import brocoeur.example.analytics.model.Game;
import brocoeur.example.analytics.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostConstruct
    public void saveGames() {
        // Clean-up
        gameService.deleteAllGames();

        // Initialization
        List<Game> games = new ArrayList<>();
        games.add(new Game(123, "Roulette"));
        games.add(new Game(324, "Coin Toss"));
        games.add(new Game(666, "Black Jack"));
        games.add(new Game(420, "Poker"));
        gameService.initializeGames(games);
    }
}
