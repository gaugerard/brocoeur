package brocoeur.example.games.service;

import brocoeur.example.common.GameStrategyTypes;
import brocoeur.example.common.GameTypes;
import brocoeur.example.common.OfflineGameStrategyTypes;
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

    public List<PlayerResponse> playDirectGame(final ServiceRequest serviceRequest) {
        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final GameStrategyTypes gameStrategyTypes = playerRequest.getGameStrategyTypes();
        if (gameStrategyTypes == null) {
            throw new IllegalStateException("Missing mandatory 'GameStrategyTypes'.");
        }
        final GameTypes gameTypes = gameStrategyTypes.getGameTypes();

        return switch (gameTypes) {
            case COIN_TOSS -> coinTossService.playCoinTossGame(serviceRequest);
            case ROULETTE -> rouletteService.playRouletteGame(serviceRequest);
            case BLACK_JACK -> blackJackService.playBlackJackGame(serviceRequest);
            case POKER -> throw new IllegalStateException("Poker should not be managed here.");
        };
    }

    public List<PlayerResponse> playOfflineGame(final ServiceRequest serviceRequest, final List<Boolean> listOfIsWinner) {
        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final OfflineGameStrategyTypes offlineGameStrategyTypes = playerRequest.getOfflineGameStrategyTypes();
        if (offlineGameStrategyTypes == null) {
            throw new IllegalStateException("Missing mandatory 'OfflineGameStrategyTypes'.");
        }
        final GameTypes gameTypes = offlineGameStrategyTypes.getGameTypes();

        return switch (gameTypes) {
            case COIN_TOSS -> coinTossService.playCoinTossGame(serviceRequest, listOfIsWinner);
            case ROULETTE -> rouletteService.playRouletteGame(serviceRequest, listOfIsWinner);
            case BLACK_JACK -> throw new IllegalStateException("Offline BlackJack not be managed for the moment.");
            case POKER -> throw new IllegalStateException("Offline Poker not be managed for the moment.");
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
