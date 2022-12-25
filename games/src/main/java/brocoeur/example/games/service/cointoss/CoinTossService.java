package brocoeur.example.games.service.cointoss;


import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.cointoss.CoinTossPlay;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.games.service.GameRound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class CoinTossService implements GameRound {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinTossService.class);

    @Override
    public GamePlay play() {
        return Arrays.stream(CoinTossPlay.values()).toList().get(new Random().nextInt(CoinTossPlay.values().length));
    }

    public GamePlay play(GameStrategy gameStrategy) {
        return gameStrategy.play();
    }

    public PlayerResponse play(final PlayerRequest player) {
        final GamePlay userPlay = player.getGameStrategyTypes().getGameStrategy().play();
        final GamePlay servicePlay = play();
        final boolean isWinner = servicePlay.equals(userPlay);
        return new PlayerResponse(
                324,
                Integer.parseInt(player.getUserId()),
                isWinner,
                player.getAmountToGamble(),
                player.getLinkedJobId());
    }

    public PlayerResponse play(final PlayerRequest player, final List<Boolean> listOfIsWinner) {
        final GamePlay userPlay = player.getOfflineGameStrategyTypes().getOfflineGameStrategy().playOffline(listOfIsWinner);
        final GamePlay servicePlay = play();
        final boolean isWinner = servicePlay.equals(userPlay);
        return new PlayerResponse(
                324,
                Integer.parseInt(player.getUserId()),
                isWinner,
                player.getAmountToGamble(),
                player.getLinkedJobId());
    }

    @Override
    public boolean didPlayerWin(GamePlay userPlay, GamePlay servicePlay) {
        return userPlay.equals(servicePlay);
    }

    /**
     * Use for <b>Direct</b> play.
     */
    public List<PlayerResponse> playCoinTossGame(final ServiceRequest serviceRequest) {
        LOGGER.info("Game of coin-toss started for : {}", serviceRequest);

        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final PlayerResponse playerResponse = play(playerRequest);

        LOGGER.info("Game of coin-toss ended with response : {}", playerResponse);

        return List.of(playerResponse);
    }

    /**
     * Use for <b>Offline</b> play.
     */
    public List<PlayerResponse> playCoinTossGame(final ServiceRequest serviceRequest, final List<Boolean> listOfIsWinner) {
        LOGGER.info("Game of coin-toss started for : {}", serviceRequest);

        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final PlayerResponse playerResponse = play(playerRequest, listOfIsWinner);

        LOGGER.info("Game of coin-toss ended with response : {}", playerResponse);

        return List.of(playerResponse);
    }
}
