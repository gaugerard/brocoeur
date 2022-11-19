package brocoeur.example.broker.common;

import java.util.List;

public interface OfflineGameStrategy {

    public GamePlay getOfflineStrategyPlay(List<GamePlay> listOfPreviousGameResult);
}
