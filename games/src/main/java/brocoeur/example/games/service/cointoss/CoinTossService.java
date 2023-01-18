package brocoeur.example.games.service.cointoss;


import brocoeur.example.common.CoinTossGameStrategy;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.cointoss.CoinTossPlay;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class CoinTossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinTossService.class);

    /**
     * CoinToss 2.0 better gambling management (BGM)
     * For both DIRECT and OFFLINE (DIRECT is OFFLINE with TTL of 1).
     */
    public List<PlayerResponse> play(final PlayerRequest playerRequest, final int ttl) {
        final List<CoinTossPlay> previousCoinTossPlay = new ArrayList<>();

        int availableMoney = playerRequest.getAmountToGamble();
        final CoinTossGameStrategy coinTossGameStrategy = (CoinTossGameStrategy) playerRequest.getGameStrategyTypes().getGameStrategy();
        LOGGER.info("Game of coin-toss started with availableMoney: {}, strategy : {}, ttl :{}", availableMoney, coinTossGameStrategy, ttl);

        for (var i = 0; i < ttl; i++) {
            final Gamble gamble = coinTossGameStrategy.play(availableMoney, previousCoinTossPlay);
            availableMoney -= gamble.amount();

            final CoinTossPlay servicePlay = playServiceCoinToss();
            previousCoinTossPlay.add(servicePlay);

            final int amountWon = getAmountWon(gamble, servicePlay);
            availableMoney += amountWon;
            LOGGER.info("Player actions for ttl id : {}, actions : {}, game result (with previous results) : {}", i, gamble, previousCoinTossPlay);
            LOGGER.info("Player amount won and new available money for ttl id : {}, amountWon : {}, availableMoney : {}", i, amountWon, availableMoney);
        }
        LOGGER.info("Game of coin-toss ended with availableMoney: {}, strategy : {}, ttl :{}", availableMoney, coinTossGameStrategy, ttl);

        final PlayerResponse playerResponse = new PlayerResponse(
                324,
                Integer.parseInt(playerRequest.getUserId()),
                playerRequest.getAmountToGamble(),
                availableMoney,
                playerRequest.getLinkedJobId()
        );

        LOGGER.info("Game of coin-toss ended with response: {}", playerResponse);

        return List.of(playerResponse);
    }

    private int getAmountWon(final Gamble gamble, final CoinTossPlay servicePlay) {
        int amountWon = 0;

        final CoinTossPlay coinTossPlay = (CoinTossPlay) gamble.gamePlay();
        final int amount = gamble.amount();

        if (coinTossPlay.equals(servicePlay)) {
            amountWon += coinTossPlay.getMultiplier() * amount;
        }

        return amountWon;
    }

    private CoinTossPlay playServiceCoinToss() {
        return Arrays.stream(CoinTossPlay.values()).toList().get(new Random().nextInt(CoinTossPlay.values().length));
    }
}
