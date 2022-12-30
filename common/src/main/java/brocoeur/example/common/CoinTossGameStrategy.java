package brocoeur.example.common;

import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.List;

public interface CoinTossGameStrategy extends GameStrategy {

    public Gamble play(final int availableMoney, final List<CoinTossPlay> previousRoulettePlay);
}