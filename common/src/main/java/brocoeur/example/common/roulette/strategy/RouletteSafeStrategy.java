package brocoeur.example.common.roulette.strategy;

import brocoeur.example.common.Gamble;
import brocoeur.example.common.RouletteGameStrategy;
import brocoeur.example.common.roulette.RoulettePlay;

import java.util.ArrayList;
import java.util.List;

public class RouletteSafeStrategy implements RouletteGameStrategy {

    private void addNewGamble(final RoulettePlay roulettePlay, final int requestedAmount, final int availableMoney, final List<Gamble> gambleList) {
        final int amountToGamble = gambleList.stream().mapToInt(Gamble::amount).sum();
        if ((amountToGamble + requestedAmount) <= availableMoney) {
            gambleList.add(new Gamble(roulettePlay, requestedAmount));
        }
    }

    @Override
    public List<Gamble> play(final int availableMoney, final List<RoulettePlay> previousRoulettePlay) {
        final List<Gamble> gambleList = new ArrayList<>();
        // Plays RED.
        addNewGamble(RoulettePlay.RED, 5, availableMoney, gambleList);

        // Plays small amount on GREEN, except if GREEN was there recently.
        if (previousRoulettePlay.contains(RoulettePlay.ZERO)) {
            addNewGamble(RoulettePlay.ZERO, 5, availableMoney, gambleList);
        } else {
            addNewGamble(RoulettePlay.ZERO, 1, availableMoney, gambleList);
        }

        return List.copyOf(gambleList);
    }
}
