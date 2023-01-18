package brocoeur.example.common.poker.strategy;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.PokerGameStrategy;
import brocoeur.example.common.poker.PokerPlay;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomPoker implements PokerGameStrategy {

    @Override
    public Gamble play(final int availableMoney, List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards) {
        final PokerPlay randomPokerPlay = Arrays.stream(PokerPlay.values()).toList().get(new Random().nextInt(PokerPlay.values().length));
        return new Gamble(randomPokerPlay, new Random().nextInt(availableMoney));
    }
}
