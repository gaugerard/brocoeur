package brocoeur.example.common.roulette.strategy;

import brocoeur.example.common.Gamble;
import brocoeur.example.common.RouletteGameStrategy;
import brocoeur.example.common.roulette.RoulettePlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RouletteRiskyStrategy implements RouletteGameStrategy {

    private void addNewGamble(final RoulettePlay roulettePlay, final int requestedAmount, final int availableMoney, final List<Gamble> gambleList) {
        final int amountToGamble = gambleList.stream().mapToInt(Gamble::amount).sum();
        if ((amountToGamble + requestedAmount) <= availableMoney) {
            gambleList.add(new Gamble(roulettePlay, requestedAmount));
        }
    }

    private boolean last3RoulettePlayAllSpecificColor(final List<RoulettePlay> last3RoulettePlay, final String color) {
        for (RoulettePlay roulettePlay : last3RoulettePlay) {
            if (!roulettePlay.getColor().equals(color)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Gamble> play(final int availableMoney, final List<RoulettePlay> previousRoulettePlay) {
        final List<Gamble> gambleList = new ArrayList<>();

        // Plays ZERO if ZERO appeared at least once.
        if (previousRoulettePlay.contains(RoulettePlay.ZERO)) {
            addNewGamble(RoulettePlay.ZERO, 2, availableMoney, gambleList);
        }

        // Plays RED if last 3 were BLACK.
        // Plays BLACK if last 3 were RED.
        if (previousRoulettePlay.size() >= 3) {
            final List<RoulettePlay> last3RoulettePlay = previousRoulettePlay.subList(previousRoulettePlay.size() - 3, previousRoulettePlay.size() - 1);
            if (last3RoulettePlayAllSpecificColor(last3RoulettePlay, "RED")) {
                addNewGamble(RoulettePlay.BLACK, 10, availableMoney, gambleList);
            }
            if (last3RoulettePlayAllSpecificColor(last3RoulettePlay, "BLACK")) {
                addNewGamble(RoulettePlay.RED, 10, availableMoney, gambleList);
            }
        }

        // Else, plays 2 random NUMBER.
        if (gambleList.size() == 0) {
            for (int i = 0; i < 2; i++) {
                final RoulettePlay randomRoulettePlay = Arrays.stream(RoulettePlay.values()).toList().get(new Random().nextInt(RoulettePlay.values().length));
                addNewGamble(randomRoulettePlay, 1, availableMoney, gambleList);
            }
        }

        return List.copyOf(gambleList);
    }
}
