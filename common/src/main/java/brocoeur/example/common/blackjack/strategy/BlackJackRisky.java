package brocoeur.example.common.blackjack.strategy;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.blackjack.BlackJackPlay;
import brocoeur.example.common.blackjack.BlackJackUtils;
import lombok.ToString;

import java.util.List;

import static brocoeur.example.common.blackjack.BlackJackPlay.MORE;
import static brocoeur.example.common.blackjack.BlackJackPlay.STOP;

@ToString
public class BlackJackRisky implements GameStrategy {
    @Override
    public BlackJackPlay play() {
        return BlackJackPlay.MORE;
    }

    @Override
    public BlackJackPlay play(final List<DeckOfCards.Card> playerCards) {
        final int totalScore = BlackJackUtils.getTotalScore(playerCards);
        return totalScore >= 20 ? STOP : MORE;
    }

    @Override
    public BlackJackPlay play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards) {
        return null;
    }
}
