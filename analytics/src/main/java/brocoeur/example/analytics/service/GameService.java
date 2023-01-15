package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.Game;
import brocoeur.example.analytics.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private GameRepository gameRepository;

    public void initializeGames(List<Game> games) {
        gameRepository.saveAll(games).subscribe(updated -> LOGGER.info("Initializing : {}", updated));
    }

    public void deleteAllGames() {
        gameRepository.deleteAll().subscribe();
    }
}
