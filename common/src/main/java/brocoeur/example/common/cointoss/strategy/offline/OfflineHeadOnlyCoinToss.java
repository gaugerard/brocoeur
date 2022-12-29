package brocoeur.example.common.cointoss.strategy.offline;

import brocoeur.example.common.Gamble;
import brocoeur.example.common.OfflineGameStrategy;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.List;

public class OfflineHeadOnlyCoinToss implements OfflineGameStrategy {

    @Override
    public Gamble playOffline(int availableAmount, List<Boolean> listOfPreviousIsWinner) {
        return new Gamble(CoinTossPlay.HEAD, availableAmount);
    }

    @Override
    public Gamble playSingleOrMultiple(int availableAmount, List<Boolean> listOfPreviousIsWinner) {
        return null;
    }
}
