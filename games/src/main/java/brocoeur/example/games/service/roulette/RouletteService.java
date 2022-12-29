package brocoeur.example.games.service.roulette;

import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.request.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouletteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouletteService.class);

    @Autowired
    private EuropeanRoulette europeanRoulette;

    /**
     * Use for <b>Direct</b> play.
     */
    public List<PlayerResponse> playRouletteGame(final ServiceRequest serviceRequest) {
        LOGGER.info("Game of roulette started for : {}", serviceRequest);

        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final PlayerResponse playerResponse = europeanRoulette.play(playerRequest);

        LOGGER.info("Game of roulette ended with response : {}", playerResponse);

        return List.of(playerResponse);
    }

    /**
     * Use for <b>Offline</b> play.
     */
    public List<PlayerResponse> playRouletteGame(final ServiceRequest serviceRequest, final List<Boolean> listOfIsWinner) {
        LOGGER.info("Game of roulette started for : {}", serviceRequest);

        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final PlayerResponse playerResponse = europeanRoulette.play(playerRequest, listOfIsWinner);

        LOGGER.info("Game of roulette ended with response : {}", playerResponse);

        return List.of(playerResponse);
    }
}
