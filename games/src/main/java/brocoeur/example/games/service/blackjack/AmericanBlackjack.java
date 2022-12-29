package brocoeur.example.games.service.blackjack;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.blackjack.BlackJackPlay;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AmericanBlackjack {

    @Autowired
    private DeckOfCards deckOfCards;

    private int getInitialAmountGambled(final int amountToGamble) {
        if (amountToGamble % 2 == 0) {
            return amountToGamble / 2;
        }
        return (amountToGamble - 1) / 2;
    }

    public PlayerResponse play(final PlayerRequest player) {
        int initialAmountGambled = getInitialAmountGambled(player.getAmountToGamble());
        final List<DeckOfCards.Card> cardPlayer = new ArrayList<>(List.of(deckOfCards.getCard()));
        int availableAmount = player.getAmountToGamble() - initialAmountGambled;

        boolean isGameOver = false;

        while (!isGameOver && getTotalScore(cardPlayer) <= 21) {
            final Gamble gamble = player.getGameStrategyTypes().getGameStrategy().play(cardPlayer, availableAmount);
            final BlackJackPlay blackJackPlay = (BlackJackPlay) gamble.getFirstGamePlay();

            if (BlackJackPlay.HIT.equals(blackJackPlay)) {
                cardPlayer.add(deckOfCards.getCard());
            }

            if (BlackJackPlay.DOUBLE.equals(blackJackPlay)) {
                cardPlayer.add(deckOfCards.getCard());
                availableAmount -= initialAmountGambled;
                initialAmountGambled = 2 * initialAmountGambled;
                isGameOver = true;
            }

            if (BlackJackPlay.STOP.equals(blackJackPlay)) {
                isGameOver = true;
            }
        }

        final List<DeckOfCards.Card> cardCasino = List.of(deckOfCards.getCard(), deckOfCards.getCard());
        final boolean isWinner = isPlayerWinner(cardPlayer, cardCasino);

        final int amountWon = availableAmount + (isWinner ? initialAmountGambled * 2 : 0);

        return new PlayerResponse(
                666,
                Integer.parseInt(player.getUserId()),
                isWinner,
                amountWon,
                player.getLinkedJobId());
    }

    private boolean isPlayerWinner(final List<DeckOfCards.Card> cardPlayer, final List<DeckOfCards.Card> cardCasino) {
        final int totalScorePlayer = getTotalScore(cardPlayer);
        final int totalScoreCasino = getTotalScore(cardCasino);

        return totalScorePlayer >= totalScoreCasino && totalScorePlayer <= 21;
    }

    private int getTotalScore(final List<DeckOfCards.Card> cards) {
        int totalScore = 0;
        for (DeckOfCards.Card card : cards) {
            totalScore += card.getValue();
        }
        return totalScore;
    }
}
