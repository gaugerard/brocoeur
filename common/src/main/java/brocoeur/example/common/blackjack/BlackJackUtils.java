package brocoeur.example.common.blackjack;

import brocoeur.example.common.DeckOfCards;

import java.util.List;

public final class BlackJackUtils {

    public static int getTotalScore(final List<DeckOfCards.Card> cards) {
        int totalScore = 0;
        for (DeckOfCards.Card card : cards) {
            totalScore += card.getValue();
        }
        return totalScore;
    }
}
