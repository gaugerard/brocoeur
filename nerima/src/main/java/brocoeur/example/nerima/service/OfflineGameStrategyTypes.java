package brocoeur.example.nerima.service;

import brocoeur.example.nerima.service.cointoss.strategy.offline.OfflineRandomCoinToss;

public enum OfflineGameStrategyTypes {

    // Offline Coin Toss
    OFFLINE_COIN_TOSS_RANDOM(new OfflineRandomCoinToss(), GameTypes.COIN_TOSS);

    private final OfflineGameStrategy offlineGameStrategy;
    private final GameTypes gameTypes;

    public OfflineGameStrategy getOfflineGameStrategy() {
        return offlineGameStrategy;
    }

    public GameTypes getGameTypes() {
        return gameTypes;
    }

    OfflineGameStrategyTypes(final OfflineGameStrategy offlineGameStrategy,final GameTypes gameTypes) {
        this.offlineGameStrategy = offlineGameStrategy;
        this.gameTypes = gameTypes;
    }
}
