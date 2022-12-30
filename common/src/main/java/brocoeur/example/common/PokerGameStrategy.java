package brocoeur.example.common;

import java.util.List;

public interface PokerGameStrategy extends GameStrategy {
    public Gamble play(final int availableMoney, List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards);
}
