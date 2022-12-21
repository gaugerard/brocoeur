package brocoeur.example.games.service;

import brocoeur.example.common.GameTypes;
import brocoeur.example.common.cointoss.CoinTossPlay;
import brocoeur.example.common.roulette.RoulettePlay;
import brocoeur.example.games.service.cointoss.CoinTossService;
import brocoeur.example.games.service.roulette.RouletteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static brocoeur.example.common.poker.PokerPlay.STOP;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private CoinTossService coinTossServiceMock;
    @Mock
    private RouletteService rouletteServiceMock;

    @InjectMocks
    private GameService gameService;

    @Test
    void shouldTestGameServicePlayForCoinToss() {
        // Given
        var gameTypes = GameTypes.COIN_TOSS;

        when(coinTossServiceMock.play()).thenReturn(CoinTossPlay.TAIL);

        // When
        var actualGamePlay = gameService.play(gameTypes);

        // Then
        Assertions.assertEquals(CoinTossPlay.TAIL, actualGamePlay);
        Mockito.verifyNoInteractions(rouletteServiceMock);
    }

    @Test
    void shouldTestGameServicePlayForRoulette() {
        // Given
        var gameTypes = GameTypes.ROULETTE;

        when(rouletteServiceMock.play()).thenReturn(RoulettePlay.GREEN);

        // When
        var actualGamePlay = gameService.play(gameTypes);

        // Then
        Assertions.assertEquals(RoulettePlay.GREEN, actualGamePlay);
        Mockito.verifyNoInteractions(coinTossServiceMock);
    }

    @Test
    void shouldTestGameServicePlayForPoker() {
        // Given
        var gameTypes = GameTypes.POKER;

        // When
        var actualGamePlay = gameService.play(gameTypes);

        // Then
        Assertions.assertEquals(STOP, actualGamePlay);
        Mockito.verifyNoInteractions(coinTossServiceMock);
        Mockito.verifyNoInteractions(rouletteServiceMock);
    }
}
