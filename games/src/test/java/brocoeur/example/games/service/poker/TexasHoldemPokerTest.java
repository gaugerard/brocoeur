package brocoeur.example.games.service.poker;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static brocoeur.example.common.GameStrategyTypes.POKER_RANDOM;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
class TexasHoldemPokerTest {

    @Mock
    private DeckOfCards deckOfCardsMock;

    @InjectMocks
    private TexasHoldemPoker texasHoldemPoker;

    @Test
    void shouldTest() {
        // Given
        var player1 = new PlayerRequest("8", POKER_RANDOM, null, 50, 1);
        var player2 = new PlayerRequest("9", POKER_RANDOM, null, 60, 2);
        var player3 = new PlayerRequest("10", POKER_RANDOM, null, 70, 3);

        Mockito.when(deckOfCardsMock.getCard())
                .thenReturn(new DeckOfCards.Card(1))
                .thenReturn(new DeckOfCards.Card(2))
                .thenReturn(new DeckOfCards.Card(3))
                .thenReturn(new DeckOfCards.Card(4))
                .thenReturn(new DeckOfCards.Card(5))
                .thenReturn(new DeckOfCards.Card(6));

        // When
        var listOfPlayerResponse = texasHoldemPoker.play(player1, player2, player3);

        // Then
        var playerResponse1 = new PlayerResponse(420, 8, false, 50, 1);
        var playerResponse2 = new PlayerResponse(420, 9, false, 60, 2);
        var playerResponse3 = new PlayerResponse(420, 10, true, 70, 3);
        var expectedListOfPlayerResponse = List.of(playerResponse1, playerResponse2, playerResponse3);
        MatcherAssert.assertThat(listOfPlayerResponse, equalTo(expectedListOfPlayerResponse));
    }
}
