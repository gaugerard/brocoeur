package brocoeur.example.games.controller;

import brocoeur.example.ConfigProperties;
import brocoeur.example.games.service.GameService;
import brocoeur.example.nerima.controller.ServiceRequest;
import brocoeur.example.nerima.service.roulette.RoulettePlay;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static brocoeur.example.nerima.service.GameStrategyTypes.ROULETTE_RISKY;


@ExtendWith(MockitoExtension.class)
class GamesControllerTest {

    @Mock
    private GameService gameServiceMock;
    @Mock
    private ConfigProperties configPropertiesMock;
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
    }
}
