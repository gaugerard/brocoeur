package brocoeur.example.games.service.roulette;

import brocoeur.example.common.Gamble;
import brocoeur.example.common.RouletteGameStrategy;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.roulette.RoulettePlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class RouletteService {
    private static final List<RoulettePlay> COLOR_TO_EXCLUDE = List.of(
            RoulettePlay.RED,
            RoulettePlay.BLACK);
    private static final Logger LOGGER = LoggerFactory.getLogger(RouletteService.class);

    /**
     * Roulette 2.0 better gambling management (BGM)
     * For both DIRECT and OFFLINE (DIRECT is OFFLINE with TTL of 1).
     */
    public List<PlayerResponse> play(final PlayerRequest playerRequest, final int ttl) {
        LOGGER.info("Game of roulette started for playerRequest: {} with ttl of: {}", playerRequest, ttl);

        final List<RoulettePlay> previousRoulettePlay = new ArrayList<>();

        int availableMoney = playerRequest.getAmountToGamble();
        final RouletteGameStrategy rouletteGameStrategy = (RouletteGameStrategy) playerRequest.getGameStrategyTypes().getGameStrategy();

        for (var i = 0; i < ttl; i++) {
            final List<Gamble> gambleList = rouletteGameStrategy.play(availableMoney, previousRoulettePlay);
            availableMoney -= gambleList.stream().mapToInt(Gamble::amount).sum();

            final RoulettePlay servicePlay = playServiceRoulette();
            previousRoulettePlay.add(servicePlay);

            final int amountWon = getAmountWon(gambleList, servicePlay);
            availableMoney += amountWon;
        }
        final PlayerResponse playerResponse = new PlayerResponse(
                123,
                Integer.parseInt(playerRequest.getUserId()),
                playerRequest.getAmountToGamble(),
                availableMoney,
                playerRequest.getLinkedJobId()
        );

        LOGGER.info("Game of roulette ended with response: {}", playerResponse);

        return List.of(playerResponse);
    }

    private int getAmountWon(final List<Gamble> gambleList, final RoulettePlay servicePlay) {
        int amountWon = 0;

        for (Gamble gamble : gambleList) {
            final RoulettePlay roulettePlay = (RoulettePlay) gamble.gamePlay();
            final int amount = gamble.amount();

            // If player played a color.
            if (roulettePlay.equals(RoulettePlay.RED) || roulettePlay.equals(RoulettePlay.BLACK)) {
                if (roulettePlay.getColor().equals(servicePlay.getColor())) {
                    amountWon += roulettePlay.getMultiplier() * amount;
                }
            }

            // If player played a number.
            else {
                if (roulettePlay.equals(servicePlay)) {
                    amountWon += roulettePlay.getMultiplier() * amount;
                }
            }
        }
        return amountWon;
    }

    private RoulettePlay playServiceRoulette() {
        return Arrays.stream(RoulettePlay.values()).filter(value -> !COLOR_TO_EXCLUDE.contains(value)).toList().get(new Random().nextInt(RoulettePlay.values().length));
    }
}
