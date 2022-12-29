package brocoeur.example.common.blackjack.strategy;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.blackjack.BlackJackPlay;
import brocoeur.example.common.blackjack.BlackJackUtils;
import lombok.ToString;

import java.util.List;

import static brocoeur.example.common.blackjack.BlackJackPlay.HIT;
import static brocoeur.example.common.blackjack.BlackJackPlay.STOP;

@ToString
public class BlackJackSafe implements GameStrategy {
    @Override
    public Gamble play(int availableAmount) {
        return new Gamble(BlackJackPlay.STOP, 0);
    }

    @Override
    public Gamble playSingleOrMultiple(int availableAmount) {
        return null;
    }

    @Override
    public Gamble play(final List<DeckOfCards.Card> playerCards, int availableAmount) {
        final int totalScore = BlackJackUtils.getTotalScore(playerCards);
        final BlackJackPlay blackJackPlay = totalScore >= 15 ? STOP : HIT;
        return new Gamble(blackJackPlay, 0);
    }

    @Override
    public Gamble play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards, int availableAmount) {
        return null;
    }
}
