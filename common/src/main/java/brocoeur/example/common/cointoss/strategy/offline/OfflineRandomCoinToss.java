package brocoeur.example.common.cointoss.strategy.offline;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.OfflineGameStrategy;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.List;

public class OfflineRandomCoinToss implements OfflineGameStrategy {

    @Override
    public GamePlay getOfflineStrategyPlay(final List<GamePlay> listOfPreviousGameResult) {
        return (listOfPreviousGameResult.size() % 2 == 0) ? CoinTossPlay.HEAD : CoinTossPlay.TAIL;
    }

    @Override
    public String toString() {
        return "OfflineRandomCoinToss";
    }
}
