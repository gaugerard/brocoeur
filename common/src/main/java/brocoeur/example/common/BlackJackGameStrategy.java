package brocoeur.example.common;

import java.util.List;

public interface BlackJackGameStrategy extends GameStrategy {

    public Gamble play(final int availableMoney, final Integer initialBet, final List<DeckOfCards.Card> playerCards);
}
