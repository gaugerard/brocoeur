package brocoeur.example.common.roulette.strategy.direct;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.roulette.RoulettePlay;

import java.util.List;

public class RouletteSafeStrategy implements GameStrategy {
    @Override
    public Gamble play(int availableAmount) {
        return null;
    }

    @Override
    public Gamble playSingleOrMultiple(int availableAmount) {
        return new Gamble(RoulettePlay.RED, availableAmount);
    }

    @Override
    public Gamble play(List<DeckOfCards.Card> playerCards, int availableAmount) {
        return null;
    }

    @Override
    public Gamble play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards, int availableAmount) {
        return null;
    }

    @Override
    public String toString() {
        return "RouletteSafeStrategy";
    }
}
