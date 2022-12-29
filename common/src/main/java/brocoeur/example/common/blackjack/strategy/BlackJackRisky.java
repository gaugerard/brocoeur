package brocoeur.example.common.blackjack.strategy;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.blackjack.BlackJackPlay;
import lombok.ToString;

import java.util.List;

import static brocoeur.example.common.blackjack.BlackJackPlay.DOUBLE;

@ToString
public class BlackJackRisky implements GameStrategy {
    @Override
    public Gamble play(int availableAmount) {
        return new Gamble(BlackJackPlay.HIT, 0);
    }

    @Override
    public Gamble playSingleOrMultiple(int availableAmount) {
        return null;
    }

    @Override
    public Gamble play(final List<DeckOfCards.Card> playerCards, int availableAmount) {
        return new Gamble(DOUBLE, 0);
    }

    @Override
    public Gamble play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards, int availableAmount) {
        return null;
    }
}
