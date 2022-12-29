package brocoeur.example.common.poker.direct;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.poker.PokerPlay;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static brocoeur.example.common.poker.PokerPlay.CHECK;

public class RandomPoker implements GameStrategy {
    @Override
    public Gamble play(int availableAmount) {
        final PokerPlay pokerPlay = Arrays.stream(PokerPlay.values()).toList().get(new Random().nextInt(PokerPlay.values().length));
        return new Gamble(pokerPlay, availableAmount);
    }

    @Override
    public Gamble playSingleOrMultiple(int availableAmount) {
        return null;
    }

    @Override
    public Gamble play(List<DeckOfCards.Card> playerCards, int availableAmount) {
        return null;
    }

    @Override
    public Gamble play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards, int availableAmount) {
        return new Gamble(CHECK, 0);
    }

    @Override
    public String toString() {
        return "RandomPoker";
    }
}
