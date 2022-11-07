package brocoeur.example.games.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class GamesControllerTest {

    /*@Mock
    private GameService gameServiceMock;
    @Mock
    private GamesConfigProperties gamesConfigPropertiesMock;
    @InjectMocks
    private GamesController gamesController;

    @Test
    void shouldTestGetMsgForWinningUser() {
        // Given
        var serviceRequest = new ServiceRequest("12345", ROULETTE_RISKY);

        Mockito.when(gameServiceMock.play(serviceRequest.getGameStrategyTypes().getGameTypes())).thenReturn(RoulettePlay.GREEN);

        // When
        var actualGameResult = gamesController.getMsg(serviceRequest);

        // Then
        MatcherAssert.assertThat(actualGameResult, Matchers.equalTo("User WON !"));
    }

    @Test
    void shouldTestGetMsgForLosingUser() {
        // Given
        var serviceRequest = new ServiceRequest("12345", ROULETTE_RISKY);

        Mockito.when(gameServiceMock.play(serviceRequest.getGameStrategyTypes().getGameTypes())).thenReturn(RoulettePlay.RED);

        // When
        var actualGameResult = gamesController.getMsg(serviceRequest);

        // Then
        MatcherAssert.assertThat(actualGameResult, Matchers.equalTo("User LOST !"));
    }*/
}
