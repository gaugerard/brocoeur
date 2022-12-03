package brocoeur.example.broker.common.roulette.strategy.offline;

import brocoeur.example.broker.common.GamePlay;
import brocoeur.example.broker.common.OfflineGameStrategy;

import java.util.List;

import static brocoeur.example.broker.common.roulette.RoulettePlay.GREEN;

public class OfflineGreenOnlyRoulette implements OfflineGameStrategy {

    @Override
    public GamePlay getOfflineStrategyPlay(List<GamePlay> listOfPreviousGameResult) {
        return GREEN;
    }
}
