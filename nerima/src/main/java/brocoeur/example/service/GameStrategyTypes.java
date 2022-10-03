package brocoeur.example;

import brocoeur.example.service.cointoss.strategy.RandomCoinToss;
import brocoeur.example.service.roulette.strategy.RouletteRiskyStrategy;
import brocoeur.example.service.roulette.strategy.RouletteSafeStrategy;

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
