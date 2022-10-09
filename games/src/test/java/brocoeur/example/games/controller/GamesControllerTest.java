package brocoeur.example.games.controller;

import brocoeur.example.games.service.GameService;
import brocoeur.example.nerima.service.GamePlay;
import brocoeur.example.nerima.service.GameTypes;
import brocoeur.example.nerima.service.cointoss.CoinTossPlay;
import brocoeur.example.nerima.service.roulette.RoulettePlay;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
public class GamesControllerTest {

    @Mock
    private GameService gameServiceMock;
    @InjectMocks
    private GamesController gamesController;

    public static Stream<Arguments> getInputForGameResult() {
        return Stream.of(
                Arguments.of("Test - CoinToss game result", GameTypes.COIN_TOSS, CoinTossPlay.HEAD),
                Arguments.of("Test - Roulette game result", GameTypes.ROULETTE, RoulettePlay.RED)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getInputForGameResult")
    void shouldTestCoinTossGames(final String testName, final GameTypes gameTypes, final GamePlay gamePlay){
        // Given
        Mockito.when(gameServiceMock.play(gameTypes)).thenReturn(gamePlay);

        // When
        var actualGameResult = gamesController.getGameResult(gameTypes);

        // Then
        var expectedGameResult = new ResponseEntity<>(gamePlay, HttpStatus.OK);
        MatcherAssert.assertThat(actualGameResult, Matchers.equalTo(expectedGameResult));
    }

}
