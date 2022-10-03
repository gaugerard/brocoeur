package nerima.example;

import nerima.example.cointoss.strategy.RandomCoinToss;
import nerima.example.roulette.strategy.RouletteRiskyStrategy;
import nerima.example.roulette.strategy.RouletteSafeStrategy;

public enum GameStrategyTypes {
    // Roulette
    ROULETTE_RISKY(new RouletteRiskyStrategy()),
    ROULETTE_SAFE(new RouletteSafeStrategy()),
    // Coin Toss
    COIN_TOSS_RANDOM(new RandomCoinToss());

    private final GameStrategy gameStrategy;

    public GameStrategy getGameStrategy() {
        return gameStrategy;
    }

    GameStrategyTypes(final GameStrategy gameStrategy) {
        this.gameStrategy = gameStrategy;
    }
}
