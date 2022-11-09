package brocoeur.example.nerima.service.cointoss.strategy.offline;

import brocoeur.example.nerima.service.GamePlay;
import brocoeur.example.nerima.service.offline.OfflineGameStrategy;

import java.util.List;

import static brocoeur.example.nerima.service.cointoss.CoinTossPlay.HEAD;
import static brocoeur.example.nerima.service.cointoss.CoinTossPlay.TAIL;

public class OfflineRandomCoinToss implements OfflineGameStrategy {

    @Override
    public GamePlay getOfflineStrategyPlay(final List<GamePlay> listOfPreviousGameResul) {
        return (listOfPreviousGameResul.size() % 2 == 0) ? HEAD : TAIL;
    }

    @Override
    public String toString() {
        return "OfflineRandomCoinToss";
    }
}
