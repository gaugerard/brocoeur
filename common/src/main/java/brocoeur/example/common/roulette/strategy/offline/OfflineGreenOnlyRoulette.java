package brocoeur.example.common.roulette.strategy.offline;

import brocoeur.example.common.Gamble;
import brocoeur.example.common.OfflineGameStrategy;

import java.util.List;

import static brocoeur.example.common.roulette.RoulettePlay.ZERO;

public class OfflineGreenOnlyRoulette implements OfflineGameStrategy {

    @Override
    public Gamble playOffline(int availableAmount, List<Boolean> listOfPreviousIsWinner) {
        return null;
    }

    @Override
    public Gamble playSingleOrMultiple(int availableAmount, List<Boolean> listOfPreviousIsWinner) {
        return new Gamble(ZERO, availableAmount);
    }
}
