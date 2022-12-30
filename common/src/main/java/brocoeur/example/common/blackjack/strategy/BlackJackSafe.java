package brocoeur.example.common.blackjack.strategy;

import brocoeur.example.common.BlackJackGameStrategy;
import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.blackjack.BlackJackUtils;
import lombok.ToString;

import java.util.List;

import static brocoeur.example.common.blackjack.BlackJackPlay.HIT;
import static brocoeur.example.common.blackjack.BlackJackPlay.STOP;

@ToString
public class BlackJackSafe implements BlackJackGameStrategy {

    @Override
    public Gamble play(final int availableMoney, final Integer initialBet, final List<DeckOfCards.Card> playerCards) {
        // Initial gamble.
        if (playerCards.size() == 0) {
            if (availableMoney - 10 >= 0) {
                return new Gamble(HIT, 10);
            }
            return new Gamble(STOP, availableMoney);
        }

        // Else the player either STOP if he is above 16 or HIT if under 16.
        final int totalScore = BlackJackUtils.getTotalScore(playerCards);
        return totalScore >= 16 ? new Gamble(STOP, 0) : new Gamble(HIT, 0);
    }
}
