package brocoeur.example.analytics.controller;

import brocoeur.example.analytics.model.Game;
import brocoeur.example.analytics.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        gameService.initializeGames(games);
    }

    @GetMapping("/list")
    public Flux<Game> getAllGames() {
        Flux<Game> games = gameService.getAllGames();
        return games;
    }

    @GetMapping("/{id}")
    public Mono<Game> getGameById(@PathVariable int id) {
        return gameService.getGameById(id);
    }
}
