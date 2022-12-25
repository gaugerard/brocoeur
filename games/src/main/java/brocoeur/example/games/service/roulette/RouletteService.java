package brocoeur.example.games.service.roulette;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.common.roulette.RoulettePlay;
import brocoeur.example.games.service.GameRound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class RouletteService implements GameRound {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouletteService.class);

    @Override
    public GamePlay play() {
        return Arrays.stream(RoulettePlay.values()).toList().get(new Random().nextInt(RoulettePlay.values().length));
    }

    public GamePlay play(GameStrategy gameStrategy) {
        return gameStrategy.play();
    }

    public PlayerResponse play(final PlayerRequest player) {
        final GamePlay userPlay = player.getGameStrategyTypes().getGameStrategy().play();
        final GamePlay servicePlay = play();
        final boolean isWinner = servicePlay.equals(userPlay);
        return new PlayerResponse(
                123,
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
                123,
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
    public List<PlayerResponse> playRouletteGame(final ServiceRequest serviceRequest) {
        LOGGER.info("Game of roulette started for : {}", serviceRequest);

        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final PlayerResponse playerResponse = play(playerRequest);

        LOGGER.info("Game of roulette ended with response : {}", playerResponse);

        return List.of(playerResponse);
    }

    /**
     * Use for <b>Offline</b> play.
     */
    public List<PlayerResponse> playRouletteGame(final ServiceRequest serviceRequest, final List<Boolean> listOfIsWinner) {
        LOGGER.info("Game of roulette started for : {}", serviceRequest);

        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final PlayerResponse playerResponse = play(playerRequest, listOfIsWinner);

        LOGGER.info("Game of roulette ended with response : {}", playerResponse);

        return List.of(playerResponse);
    }
}
