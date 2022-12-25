package brocoeur.example.common.blackjack.strategy;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.blackjack.BlackJackPlay;
import lombok.ToString;

import java.util.List;

import static brocoeur.example.common.blackjack.BlackJackPlay.MORE;
import static brocoeur.example.common.blackjack.BlackJackPlay.STOP;

@ToString
public class BlackJackSafe implements GameStrategy {
    @Override
    public BlackJackPlay play() {
        return BlackJackPlay.STOP;
    }

    @Override
    public BlackJackPlay play(final List<DeckOfCards.Card> playerCards) {
        final int totalScore = getTotalScore(playerCards);
        return totalScore >= 15 ? STOP : MORE;
    }

    @Override
    public BlackJackPlay play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards) {
        return null;
    }

    private int getTotalScore(final List<DeckOfCards.Card> cards) {
        int totalScore = 0;
        for (DeckOfCards.Card card : cards) {
            totalScore += card.getValue();
        }
        return totalScore;
    }
}
