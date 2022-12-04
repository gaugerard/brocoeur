package brocoeur.example.games.service;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameTypes;
import brocoeur.example.games.service.roulette.RouletteService;
import brocoeur.example.games.service.cointoss.CoinTossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameService {
    @Autowired
    private CoinTossService coinTossService;
    @Autowired
    private RouletteService rouletteService;

    public GamePlay play(final GameTypes gameTypes) {
        return switch (gameTypes){
            case COIN_TOSS -> coinTossService.play();
            case ROULETTE -> rouletteService.play();
        };
    }
}
