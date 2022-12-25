package brocoeur.example.common.roulette.strategy.offline;

import brocoeur.example.common.OfflineGameStrategy;
import brocoeur.example.common.roulette.RoulettePlay;

import java.util.List;

import static brocoeur.example.common.roulette.RoulettePlay.GREEN;

public class OfflineGreenOnlyRoulette implements OfflineGameStrategy {

    @Override
    public RoulettePlay playOffline(List<Boolean> listOfPreviousIsWinner) {
        return GREEN;
    }
}
