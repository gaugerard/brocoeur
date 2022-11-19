package brocoeur.example.games.service;

import brocoeur.example.broker.common.GameTypes;
import brocoeur.example.broker.common.cointoss.CoinTossPlay;
import brocoeur.example.broker.common.roulette.RoulettePlay;
import brocoeur.example.games.service.cointoss.CoinTossService;
import brocoeur.example.games.service.roulette.RouletteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void shouldTestGameServicePlayForCoinToss(){
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
    void shouldTestGameServicePlayForRoulette(){
        // Given
        var gameTypes = GameTypes.ROULETTE;

        when(rouletteServiceMock.play()).thenReturn(RoulettePlay.GREEN);

        // When
        var actualGamePlay = gameService.play(gameTypes);

        // Then
        Assertions.assertEquals(RoulettePlay.GREEN, actualGamePlay);
        Mockito.verifyNoInteractions(coinTossServiceMock);
    }
}
