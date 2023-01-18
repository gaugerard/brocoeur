package brocoeur.example.games.service.blackjack;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.GameStrategyTypes;
import brocoeur.example.common.request.PlayerRequest;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class AmericanBlackjackTest {

    @Mock
    private DeckOfCards deckOfCardsMock;
    @InjectMocks
    private AmericanBlackjack americanBlackjack;

    @Test
    void shouldPlayAGameOfAmericanBlackjackAndWin() {
        // Given
        var playerRequest = new PlayerRequest("5", GameStrategyTypes.BLACK_JACK_SAFE, 100, 5641465);
        var ttl = 1;

        Mockito.when(deckOfCardsMock.getCard())
                .thenReturn(new DeckOfCards.Card(10))  // player's first card
                .thenReturn(new DeckOfCards.Card(9))   // player's second card
                .thenReturn(new DeckOfCards.Card(18)); // casino's card

        // When
        var newAvailableMoney = americanBlackjack.play(playerRequest, ttl);

        // Then
        MatcherAssert.assertThat(newAvailableMoney, is(equalTo(110)));
    }

    @Test
    void shouldPlayAGameOfAmericanBlackjackAndLose() {
        // Given
        var playerRequest = new PlayerRequest("5", GameStrategyTypes.BLACK_JACK_SAFE, 100, 5641465);
        var ttl = 1;

        Mockito.when(deckOfCardsMock.getCard())
                .thenReturn(new DeckOfCards.Card(10))  // player's first card
                .thenReturn(new DeckOfCards.Card(7))   // player's second card
                .thenReturn(new DeckOfCards.Card(21)); // casino's card

        // When
        var newAvailableMoney = americanBlackjack.play(playerRequest, ttl);

        // Then
        MatcherAssert.assertThat(newAvailableMoney, is(equalTo(90)));
    }
}
