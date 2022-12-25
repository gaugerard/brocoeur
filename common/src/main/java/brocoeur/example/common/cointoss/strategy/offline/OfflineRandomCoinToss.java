package brocoeur.example.common.cointoss.strategy.offline;

import brocoeur.example.common.OfflineGameStrategy;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.List;

public class OfflineRandomCoinToss implements OfflineGameStrategy {

    @Override
    public CoinTossPlay playOffline(final List<Boolean> listOfPreviousIsWinner) {
        return (listOfPreviousIsWinner.size() % 2 == 0) ? CoinTossPlay.HEAD : CoinTossPlay.TAIL;
    }

    @Override
    public String toString() {
        return "OfflineRandomCoinToss";
    }
}
