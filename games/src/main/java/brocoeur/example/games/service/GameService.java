package brocoeur.example.games.service;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.GameTypes;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.games.service.blackjack.BlackJackService;
import brocoeur.example.games.service.cointoss.CoinTossService;
import brocoeur.example.games.service.poker.PokerService;
import brocoeur.example.games.service.roulette.RouletteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private CoinTossService coinTossService;
    @Autowired
    private RouletteService rouletteService;
    @Autowired
    private BlackJackService blackJackService;
    @Autowired
    private PokerService pokerService;

    public GamePlay play(final GameTypes gameTypes) {
        return switch (gameTypes) {
            case COIN_TOSS -> coinTossService.play();
            case ROULETTE -> rouletteService.play();
            case BLACK_JACK -> blackJackService.play();
            case POKER -> throw new IllegalStateException("Poker should not be managed here.");
        };
    }

    public GamePlay play(final GameTypes gameTypes, final GameStrategy gameStrategy) {
        return switch (gameTypes) {
            case COIN_TOSS -> coinTossService.play(gameStrategy);
            case ROULETTE -> rouletteService.play(gameStrategy);
            case BLACK_JACK -> blackJackService.play(gameStrategy);
            case POKER -> throw new IllegalStateException("Poker should not be managed here.");
        };
    }

    public boolean didPlayerWin(final GameTypes gameTypes, final GamePlay userPlay, final GamePlay servicePlay) {
        return switch (gameTypes) {
            case COIN_TOSS -> coinTossService.didPlayerWin(userPlay, servicePlay);
            case ROULETTE -> rouletteService.didPlayerWin(userPlay, servicePlay);
            case BLACK_JACK -> blackJackService.didPlayerWin(userPlay, servicePlay);
            case POKER -> throw new IllegalStateException("Poker should not be managed here.");
        };
    }

    public List<PlayerResponse> playPoker(final ServiceRequest serviceRequest) {
        return pokerService.playPokerGame(serviceRequest);
    }
}
