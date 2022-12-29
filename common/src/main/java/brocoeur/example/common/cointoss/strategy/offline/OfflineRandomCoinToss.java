package brocoeur.example.common.cointoss.strategy.offline;

import brocoeur.example.common.Gamble;
import brocoeur.example.common.OfflineGameStrategy;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.List;

public class OfflineRandomCoinToss implements OfflineGameStrategy {

    @Override
    public Gamble playOffline(int availableAmount, List<Boolean> listOfPreviousIsWinner) {
        final CoinTossPlay coinTossPlay = (listOfPreviousIsWinner.size() % 2 == 0) ? CoinTossPlay.HEAD : CoinTossPlay.TAIL;
        return new Gamble(coinTossPlay, availableAmount);
    }

    @Override
    public Gamble playSingleOrMultiple(int availableAmount, List<Boolean> listOfPreviousIsWinner) {
        return null;
    }

    @Override
    public String toString() {
        return "OfflineRandomCoinToss";
    }
}
