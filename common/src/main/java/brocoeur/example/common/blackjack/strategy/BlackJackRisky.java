package brocoeur.example.common.blackjack.strategy;

import brocoeur.example.common.BlackJackGameStrategy;
import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.blackjack.BlackJackUtils;
import lombok.ToString;

import java.util.List;

import static brocoeur.example.common.blackjack.BlackJackPlay.*;

@ToString
public class BlackJackRisky implements BlackJackGameStrategy {

    @Override
    public Gamble play(final int availableMoney, final Integer initialBet, final List<DeckOfCards.Card> playerCards) {
        // Initial gamble.
        if (playerCards.size() == 0) {
            if (availableMoney - 20 >= 0) {
                return new Gamble(HIT, 20);
            }
            return new Gamble(HIT, availableMoney);
        }

        // If total score of the player is 12, then he DOUBLES.
        final int totalScore = BlackJackUtils.getTotalScore(playerCards);
        if (totalScore == 12) {
            if (availableMoney - initialBet >= 0) {
                return new Gamble(DOUBLE, initialBet);
            }
        }

        // Else the player either STOP if he is above 20 or HIT if under 20.
        return totalScore >= 20 ? new Gamble(STOP, 0) : new Gamble(HIT, 0);
    }
}
