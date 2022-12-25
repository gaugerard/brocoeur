package brocoeur.example.common.poker.direct;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.poker.PokerPlay;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static brocoeur.example.common.poker.PokerPlay.CHECK;

public class RandomPoker implements GameStrategy {
    @Override
    public PokerPlay play() {
        return Arrays.stream(PokerPlay.values()).toList().get(new Random().nextInt(PokerPlay.values().length));
    }

    @Override
    public PokerPlay play(List<DeckOfCards.Card> playerCards) {
        return null;
    }

    @Override
    public PokerPlay play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards) {
        return CHECK;
    }

    @Override
    public String toString() {
        return "RandomPoker";
    }
}
