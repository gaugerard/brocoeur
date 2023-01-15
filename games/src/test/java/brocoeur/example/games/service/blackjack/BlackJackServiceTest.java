package brocoeur.example.games.service.blackjack;

import brocoeur.example.common.GameStrategyTypes;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
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
class BlackJackServiceTest {

    @Mock
    private AmericanBlackjack americanBlackjackMock;
    @InjectMocks
    private BlackJackService blackJackService;

    @Test
    void shouldPlayBlackJackGame() {
        // Given
        var playerRequest = new PlayerRequest("8", GameStrategyTypes.BLACK_JACK_SAFE, 50, 8465715);
        var ttl = 1;

        Mockito.when(americanBlackjackMock.play(playerRequest, ttl)).thenReturn(60);

        // When
        var listPlayerResponse = blackJackService.playBlackJackGame(playerRequest, ttl);

        // Then
        Mockito.verifyNoMoreInteractions(americanBlackjackMock);
        MatcherAssert.assertThat(listPlayerResponse.size(), is(equalTo(1)));
        MatcherAssert.assertThat(listPlayerResponse.get(0), is(equalTo(new PlayerResponse(666, 8, 50, 60, 8465715))));
    }
}
