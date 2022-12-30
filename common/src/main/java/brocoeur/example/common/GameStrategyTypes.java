package brocoeur.example.common;

import brocoeur.example.common.blackjack.strategy.BlackJackRisky;
import brocoeur.example.common.blackjack.strategy.BlackJackSafe;
import brocoeur.example.common.cointoss.strategy.HeadOnlyCoinTossBy5Euro;
import brocoeur.example.common.cointoss.strategy.RandomCoinTossBy10Euro;
import brocoeur.example.common.cointoss.strategy.RandomCoinTossByHalfAmount;
import brocoeur.example.common.poker.strategy.RandomPoker;
import brocoeur.example.common.roulette.strategy.RouletteRiskyStrategy;
import brocoeur.example.common.roulette.strategy.RouletteSafeStrategy;

public enum GameStrategyTypes {
    // Roulette
    ROULETTE_RISKY(new RouletteRiskyStrategy(), GameTypes.ROULETTE, ServiceRequestTypes.SINGLE_PLAYER),
    ROULETTE_SAFE(new RouletteSafeStrategy(), GameTypes.ROULETTE, ServiceRequestTypes.SINGLE_PLAYER),

    // Coin Toss
    COIN_TOSS_RANDOM(new RandomCoinTossByHalfAmount(), GameTypes.COIN_TOSS, ServiceRequestTypes.SINGLE_PLAYER),

    // Black Jack
    BLACK_JACK_SAFE(new BlackJackSafe(), GameTypes.BLACK_JACK, ServiceRequestTypes.SINGLE_PLAYER),
    BLACK_JACK_RISKY(new BlackJackRisky(), GameTypes.BLACK_JACK, ServiceRequestTypes.SINGLE_PLAYER),

    // Poker
    POKER_RANDOM(new RandomPoker(), GameTypes.POKER, ServiceRequestTypes.MULTIPLAYER),

    // Offline Coin Toss
    OFFLINE_COIN_TOSS_RANDOM(new RandomCoinTossBy10Euro(), GameTypes.COIN_TOSS, ServiceRequestTypes.SINGLE_PLAYER),
    OFFLINE_COIN_TOSS_HEAD_ONLY(new HeadOnlyCoinTossBy5Euro(), GameTypes.COIN_TOSS, ServiceRequestTypes.SINGLE_PLAYER);

    private final GameStrategy gameStrategy;
    private final GameTypes gameTypes;

    GameStrategyTypes(final GameStrategy gameStrategy, final GameTypes gameTypes, final ServiceRequestTypes serviceRequestTypes) {
        this.gameStrategy = gameStrategy;
        this.gameTypes = gameTypes;
    }

    public static GameStrategyTypes getGameStrategyTypesFromName(final String name) {
        for (GameStrategyTypes gameStrategyTypes : GameStrategyTypes.values()) {
            if (gameStrategyTypes.toString().equals(name)) {
                return gameStrategyTypes;
            }
        }
        return null;
    }

    public GameStrategy getGameStrategy() {
        return gameStrategy;
    }

    public GameTypes getGameTypes() {
        return gameTypes;
    }
}
