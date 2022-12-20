package brocoeur.example.games.service;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.GameTypes;
import brocoeur.example.games.service.blackjack.BlackJackService;
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
    @Autowired
    private BlackJackService blackJackService;

    public GamePlay play(final GameTypes gameTypes) {
        return switch (gameTypes){
            case COIN_TOSS -> coinTossService.play();
            case ROULETTE -> rouletteService.play();
            case BLACK_JACK -> blackJackService.play();
        };
    }

    public GamePlay play(final GameTypes gameTypes, final GameStrategy gameStrategy) {
        return switch (gameTypes){
            case COIN_TOSS -> coinTossService.play(gameStrategy);
            case ROULETTE -> rouletteService.play(gameStrategy);
            case BLACK_JACK -> blackJackService.play(gameStrategy);
        };
    }

    public boolean didPlayerWin(final GameTypes gameTypes, final GamePlay userPlay,final GamePlay servicePlay){
        return switch (gameTypes){
            case COIN_TOSS, ROULETTE -> userPlay.equals(servicePlay);
            case BLACK_JACK -> blackJackService.didPlayerWin(userPlay,servicePlay);
        };
    }
}
