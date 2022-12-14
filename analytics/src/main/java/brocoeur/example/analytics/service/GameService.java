package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.Game;
import brocoeur.example.analytics.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public void initializeGames(List<Game> games) {
        Flux<Game> savedGames = gameRepository.saveAll(games);
        savedGames.subscribe();
    }

    public void deleteAllGames() {
        gameRepository.deleteAll().subscribe();
    }
}
