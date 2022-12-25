package brocoeur.example.games.service.blackjack;

import brocoeur.example.common.DeckOfCards;
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

    public PlayerResponse play(final PlayerRequest player) {

        final List<DeckOfCards.Card> cardPlayer = new ArrayList<>(List.of(deckOfCards.getCard()));
        final BlackJackPlay blackJackPlay = (BlackJackPlay) player.getGameStrategyTypes().getGameStrategy().play(cardPlayer);

        if (BlackJackPlay.MORE.equals(blackJackPlay)) {
            cardPlayer.add(deckOfCards.getCard());
        }

        final List<DeckOfCards.Card> cardCasino = List.of(deckOfCards.getCard(), deckOfCards.getCard());
        final boolean isWinner = isPlayerWinner(cardPlayer, cardCasino);

        return new PlayerResponse(
                666,
                Integer.parseInt(player.getUserId()),
                isWinner,
                player.getAmountToGamble(),
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
