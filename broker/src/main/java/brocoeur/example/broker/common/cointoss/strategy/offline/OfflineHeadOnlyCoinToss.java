package brocoeur.example.broker.common.cointoss.strategy.offline;

import brocoeur.example.broker.common.GamePlay;
import brocoeur.example.broker.common.OfflineGameStrategy;

import java.util.List;

import static brocoeur.example.broker.common.cointoss.CoinTossPlay.HEAD;

public class OfflineHeadOnlyCoinToss implements OfflineGameStrategy {

    @Override
    public GamePlay getOfflineStrategyPlay(List<GamePlay> listOfPreviousGameResult) {
        return HEAD;
    }
}
