package brocoeur.example.games.service.poker;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TexasHoldemPoker {

    private static final Logger LOGGER = LoggerFactory.getLogger(TexasHoldemPoker.class);

    @Autowired
    private DeckOfCards deckOfCards;

    public List<PlayerResponse> play(final PlayerRequest player1, final PlayerRequest player2, final PlayerRequest player3) {
        final List<DeckOfCards.Card> cardPlayer1 = List.of(deckOfCards.getCard(), deckOfCards.getCard());
        final List<DeckOfCards.Card> cardPlayer2 = List.of(deckOfCards.getCard(), deckOfCards.getCard());
        final List<DeckOfCards.Card> cardPlayer3 = List.of(deckOfCards.getCard(), deckOfCards.getCard());

        final List<DeckOfCards.Card> cardCasino = List.of(deckOfCards.getCard(), deckOfCards.getCard(), deckOfCards.getCard());

        final Gamble gamblePlayer1 = player1.getGameStrategyTypes().getGameStrategy().play(cardPlayer1, cardCasino, player1.getAmountToGamble());
        LOGGER.info("Player1 executes : {}", gamblePlayer1.getFirstGamePlay());
        final Gamble gamblePlayer2 = player2.getGameStrategyTypes().getGameStrategy().play(cardPlayer2, cardCasino, player2.getAmountToGamble());
        LOGGER.info("Player2 executes : {}", gamblePlayer2.getFirstGamePlay());
        final Gamble gamblePlayer3 = player3.getGameStrategyTypes().getGameStrategy().play(cardPlayer3, cardCasino, player3.getAmountToGamble());
        LOGGER.info("Player3 executes : {}", gamblePlayer3.getFirstGamePlay());

        final Map<String, Object> infoPlayers = Map.of(
                "player1", player1, "cardPlayer1", cardPlayer1,
                "player2", player2, "cardPlayer2", cardPlayer2,
                "player3", player3, "cardPlayer3", cardPlayer3);

        // Insert complex poker management logic here.
        final String winnerId = getWinner(infoPlayers);

        return generateListPlayerResponse(winnerId, player1, player2, player3);
    }

    private List<PlayerResponse> generateListPlayerResponse(
            final String winnerId,
            final PlayerRequest player1,
            final PlayerRequest player2,
            final PlayerRequest player3) {

        final PlayerResponse playerResponse1 = new PlayerResponse(420, Integer.parseInt(player1.getUserId()), winnerId.equals(player1.getUserId()), player1.getAmountToGamble(), player1.getLinkedJobId());
        final PlayerResponse playerResponse2 = new PlayerResponse(420, Integer.parseInt(player2.getUserId()), winnerId.equals(player2.getUserId()), player2.getAmountToGamble(), player2.getLinkedJobId());
        final PlayerResponse playerResponse3 = new PlayerResponse(420, Integer.parseInt(player3.getUserId()), winnerId.equals(player3.getUserId()), player3.getAmountToGamble(), player3.getLinkedJobId());

        return List.of(playerResponse1, playerResponse2, playerResponse3);
    }

    /**
     * Dummy temporary method to determine which player won.
     */
    private String getWinner(Map<String, Object> infoPlayers) {
        final List<DeckOfCards.Card> cardsPlayer1 = (List<DeckOfCards.Card>) infoPlayers.get("cardPlayer1");
        final int totalPlayer1 = cardsPlayer1.stream().map(DeckOfCards.Card::getValue).mapToInt(i -> i).sum();

        final List<DeckOfCards.Card> cardsPlayer2 = (List<DeckOfCards.Card>) infoPlayers.get("cardPlayer2");
        final int totalPlayer2 = cardsPlayer2.stream().map(DeckOfCards.Card::getValue).mapToInt(i -> i).sum();

        final List<DeckOfCards.Card> cardsPlayer3 = (List<DeckOfCards.Card>) infoPlayers.get("cardPlayer3");
        final int totalPlayer3 = cardsPlayer3.stream().map(DeckOfCards.Card::getValue).mapToInt(i -> i).sum();

        if (totalPlayer1 >= totalPlayer2 && totalPlayer2 >= totalPlayer3) {
            final PlayerRequest playerRequest = (PlayerRequest) infoPlayers.get("player1");
            return playerRequest.getUserId();
        }
        if (totalPlayer2 >= totalPlayer1 && totalPlayer2 >= totalPlayer3) {
            final PlayerRequest playerRequest = (PlayerRequest) infoPlayers.get("player2");
            return playerRequest.getUserId();
        } else {
            final PlayerRequest playerRequest = (PlayerRequest) infoPlayers.get("player3");
            return playerRequest.getUserId();
        }
    }
}
