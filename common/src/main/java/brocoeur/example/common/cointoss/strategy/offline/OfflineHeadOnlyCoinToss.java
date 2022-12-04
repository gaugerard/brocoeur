package brocoeur.example.common.cointoss.strategy.offline;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.OfflineGameStrategy;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.List;

public class OfflineHeadOnlyCoinToss implements OfflineGameStrategy {

    @Override
    public GamePlay getOfflineStrategyPlay(List<GamePlay> listOfPreviousGameResult) {
        return CoinTossPlay.HEAD;
    }
}
