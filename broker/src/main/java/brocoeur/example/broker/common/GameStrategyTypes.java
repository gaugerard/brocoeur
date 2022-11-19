package brocoeur.example.broker.common;

import brocoeur.example.broker.common.cointoss.strategy.direct.RandomCoinToss;
import brocoeur.example.broker.common.roulette.strategy.direct.RouletteRiskyStrategy;
import brocoeur.example.broker.common.roulette.strategy.direct.RouletteSafeStrategy;

public enum GameStrategyTypes {
    // Roulette
    ROULETTE_RISKY(new RouletteRiskyStrategy(), GameTypes.ROULETTE),
    ROULETTE_SAFE(new RouletteSafeStrategy(), GameTypes.ROULETTE),
    // Coin Toss
    COIN_TOSS_RANDOM(new RandomCoinToss(), GameTypes.COIN_TOSS);

    private final GameStrategy gameStrategy;
    private final GameTypes gameTypes;

    public GameStrategy getGameStrategy() {
        return gameStrategy;
    }

    public GameTypes getGameTypes() {
        return gameTypes;
    }

    GameStrategyTypes(final GameStrategy gameStrategy, final GameTypes gameTypes) {
        this.gameStrategy = gameStrategy;
        this.gameTypes = gameTypes;
    }
}