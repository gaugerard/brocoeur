package brocoeur.example.common;

import brocoeur.example.common.roulette.RoulettePlay;

import java.util.List;

public interface RouletteGameStrategy extends GameStrategy {

    public List<Gamble> play(final int availableMoney, final List<RoulettePlay> previousRoulettePlay);
}
