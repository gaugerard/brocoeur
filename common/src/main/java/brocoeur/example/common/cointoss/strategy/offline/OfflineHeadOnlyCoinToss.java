package brocoeur.example.common.cointoss.strategy.offline;

import brocoeur.example.common.OfflineGameStrategy;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.List;

public class OfflineHeadOnlyCoinToss implements OfflineGameStrategy {

    @Override
    public CoinTossPlay playOffline(List<Boolean> listOfPreviousIsWinner) {
        return CoinTossPlay.HEAD;
    }
}
