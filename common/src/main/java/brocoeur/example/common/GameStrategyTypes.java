package brocoeur.example.common;

import brocoeur.example.common.blackjack.strategy.BlackJackRisky;
import brocoeur.example.common.blackjack.strategy.BlackJackSafe;
import brocoeur.example.common.cointoss.strategy.direct.RandomCoinToss;
import brocoeur.example.common.poker.direct.RandomPoker;
import brocoeur.example.common.roulette.strategy.direct.RouletteRiskyStrategy;
import brocoeur.example.common.roulette.strategy.direct.RouletteSafeStrategy;

public enum GameStrategyTypes {
    // Roulette
    ROULETTE_RISKY(new RouletteRiskyStrategy(), GameTypes.ROULETTE),
    ROULETTE_SAFE(new RouletteSafeStrategy(), GameTypes.ROULETTE),

    // Coin Toss
    COIN_TOSS_RANDOM(new RandomCoinToss(), GameTypes.COIN_TOSS),

    // Black Jack
    BLACK_JACK_SAFE(new BlackJackSafe(), GameTypes.BLACK_JACK),
    BLACK_JACK_RISKY(new BlackJackRisky(), GameTypes.BLACK_JACK),

    // Poker
    POKER_RANDOM(new RandomPoker(), GameTypes.POKER);

    private final GameStrategy gameStrategy;
    private final GameTypes gameTypes;

    GameStrategyTypes(final GameStrategy gameStrategy, final GameTypes gameTypes) {
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
