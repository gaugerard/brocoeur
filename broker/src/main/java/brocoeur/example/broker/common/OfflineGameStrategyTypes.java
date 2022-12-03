package brocoeur.example.broker.common;

import brocoeur.example.broker.common.cointoss.strategy.offline.OfflineHeadOnlyCoinToss;
import brocoeur.example.broker.common.cointoss.strategy.offline.OfflineRandomCoinToss;
import brocoeur.example.broker.common.roulette.strategy.offline.OfflineGreenOnlyRoulette;

public enum OfflineGameStrategyTypes {

    // Offline Coin Toss
    OFFLINE_COIN_TOSS_RANDOM(new OfflineRandomCoinToss(),GameTypes.COIN_TOSS),
    OFFLINE_COIN_TOSS_HEAD_ONLY(new OfflineHeadOnlyCoinToss(),GameTypes.COIN_TOSS),

    // Offline Roulette
    OFFLINE_ROULETTE_GREEN_ONLY(new OfflineGreenOnlyRoulette(),GameTypes.ROULETTE);

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
