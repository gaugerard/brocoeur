package brocoeur.example.common.roulette.strategy.offline;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.OfflineGameStrategy;

import java.util.List;

import static brocoeur.example.common.roulette.RoulettePlay.GREEN;

public class OfflineGreenOnlyRoulette implements OfflineGameStrategy {

    @Override
    public GamePlay getOfflineStrategyPlay(List<GamePlay> listOfPreviousGameResult) {
        return GREEN;
    }
}
