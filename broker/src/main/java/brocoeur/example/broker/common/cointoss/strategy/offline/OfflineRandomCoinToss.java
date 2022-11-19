package brocoeur.example.broker.common.cointoss.strategy.offline;

import brocoeur.example.broker.common.GamePlay;
import brocoeur.example.broker.common.OfflineGameStrategy;

import java.util.List;

import static brocoeur.example.broker.common.cointoss.CoinTossPlay.HEAD;
import static brocoeur.example.broker.common.cointoss.CoinTossPlay.TAIL;

public class OfflineRandomCoinToss implements OfflineGameStrategy {

    @Override
    public GamePlay getOfflineStrategyPlay(final List<GamePlay> listOfPreviousGameResult) {
        return (listOfPreviousGameResult.size() % 2 == 0) ? HEAD : TAIL;
    }

    @Override
    public String toString() {
        return "OfflineRandomCoinToss";
    }
}
