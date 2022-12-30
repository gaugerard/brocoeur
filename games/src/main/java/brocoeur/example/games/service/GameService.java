package brocoeur.example.games.service;

import brocoeur.example.common.GameStrategyTypes;
import brocoeur.example.common.GameTypes;
import brocoeur.example.common.request.PlayerRequest;
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

    public List<PlayerResponse> playGame(final ServiceRequest serviceRequest) {
        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final GameStrategyTypes gameStrategyTypes = playerRequest.getGameStrategyTypes();
        if (gameStrategyTypes == null) {
            throw new IllegalStateException("Missing mandatory 'GameStrategyTypes'.");
        }
        final GameTypes gameTypes = gameStrategyTypes.getGameTypes();

        return switch (gameTypes) {
            case COIN_TOSS ->
                    coinTossService.play(serviceRequest.getPlayerRequestList().get(0), serviceRequest.getTimeToLive());
            case ROULETTE ->
                    rouletteService.play(serviceRequest.getPlayerRequestList().get(0), serviceRequest.getTimeToLive());
            case BLACK_JACK ->
                    blackJackService.playBlackJackGame(serviceRequest.getPlayerRequestList().get(0), serviceRequest.getTimeToLive());
            case POKER -> throw new IllegalStateException("Poker should not be managed here.");
        };
    }

    public List<PlayerResponse> playMultiplayerGame(final ServiceRequest serviceRequest) {
        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final GameStrategyTypes gameStrategyTypes = playerRequest.getGameStrategyTypes();
        if (gameStrategyTypes == null) {
            throw new IllegalStateException("Missing mandatory 'GameStrategyTypes'.");
        }
        final GameTypes gameTypes = gameStrategyTypes.getGameTypes();

        return switch (gameTypes) {
            case COIN_TOSS -> throw new IllegalStateException("Coin-toss should not be managed here.");
            case ROULETTE -> throw new IllegalStateException("Roulette should not be managed here.");
            case BLACK_JACK -> throw new IllegalStateException("BlackJack should not be managed here.");
            case POKER -> pokerService.playPokerGame(serviceRequest);
        };
    }
}
