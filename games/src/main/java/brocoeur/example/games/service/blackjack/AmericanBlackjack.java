package brocoeur.example.games.service.blackjack;

import brocoeur.example.common.BlackJackGameStrategy;
import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.blackjack.BlackJackPlay;
import brocoeur.example.common.request.PlayerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AmericanBlackjack {

    @Autowired
    private DeckOfCards deckOfCards;

    public int play(final PlayerRequest playerRequest, final int ttl) {
        int availableMoney = playerRequest.getAmountToGamble();
        final BlackJackGameStrategy blackJackGameStrategy = (BlackJackGameStrategy) playerRequest.getGameStrategyTypes().getGameStrategy();

        for (int i = 0; i < ttl; i++) {
            // Player section.
            Integer initialBet = null;
            final List<Gamble> gambleList = new ArrayList<>();
            final List<DeckOfCards.Card> cardPlayer = new ArrayList<>();

            boolean isGameOver = false;
            while (!isGameOver && getTotalScore(cardPlayer) <= 21) {

                final Gamble gamble = blackJackGameStrategy.play(availableMoney, initialBet, cardPlayer);
                gambleList.add(gamble);
                final BlackJackPlay blackJackPlay = (BlackJackPlay) gamble.gamePlay();
                final int amount = gamble.amount();

                if (initialBet == null) {
                    initialBet = amount;
                    availableMoney -= amount;
                }

                if (BlackJackPlay.HIT.equals(blackJackPlay)) {
                    cardPlayer.add(deckOfCards.getCard());
                }

                if (BlackJackPlay.DOUBLE.equals(blackJackPlay)) {
                    cardPlayer.add(deckOfCards.getCard());
                    availableMoney -= amount;
                    isGameOver = true;
                }

                if (BlackJackPlay.STOP.equals(blackJackPlay)) {
                    isGameOver = true;
                }
            }

            // Casino section (STOP when >= 17).
            final List<DeckOfCards.Card> cardCasino = new ArrayList<>();
            while (getTotalScore(cardCasino) < 17) {
                cardCasino.add(deckOfCards.getCard());
            }

            final boolean isWinner = isPlayerWinner(cardPlayer, cardCasino);
            final int amountWon = getAmountWon(gambleList, isWinner, getTotalScore(cardPlayer));
            availableMoney += amountWon;
        }

        return availableMoney;
    }

    private boolean isPlayerWinner(final List<DeckOfCards.Card> cardPlayer, final List<DeckOfCards.Card> cardCasino) {
        final int totalScorePlayer = getTotalScore(cardPlayer);
        final int totalScoreCasino = getTotalScore(cardCasino);

        return totalScorePlayer >= totalScoreCasino && totalScorePlayer <= 21;
    }

    private int getAmountWon(final List<Gamble> gambleList, final boolean isWinner, final int scorePlayer) {
        if (isWinner) {
            int amountWon = 0;
            for (Gamble gamble : gambleList) {
                amountWon += gamble.amount();
            }
            final int multiplier = scorePlayer == 21 ? 3 : 2;
            return multiplier * amountWon;
        }
        return 0;
    }

    private int getTotalScore(final List<DeckOfCards.Card> cards) {
        int totalScore = 0;
        for (DeckOfCards.Card card : cards) {
            totalScore += card.getValue();
        }
        return totalScore;
    }
}
