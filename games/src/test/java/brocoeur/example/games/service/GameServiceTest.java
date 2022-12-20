package brocoeur.example.games.service;

import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.GameTypes;
import brocoeur.example.common.blackjack.BlackJackPlay;
import brocoeur.example.common.blackjack.strategy.BlackJackRisky;
import brocoeur.example.common.cointoss.CoinTossPlay;
import brocoeur.example.common.roulette.RoulettePlay;
import brocoeur.example.games.service.blackjack.BlackJackResults;
import brocoeur.example.games.service.blackjack.BlackJackService;
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
    @Mock
    private BlackJackService blackJackServiceMock;

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

    @Test
    void shouldTestGameServicePlayForBlackJack(){
        // Given
        var gameTypes = GameTypes.BLACK_JACK;
        var gameStrategy = new BlackJackRisky();

        when(blackJackServiceMock.play()).thenReturn(BlackJackResults.SEVENTEEN);
        when(blackJackServiceMock.play(gameStrategy)).thenReturn(BlackJackResults.EIGHTEEN);

        // When
        var actualBankGamePlay = gameService.play(gameTypes);
        var actualPlayerGamePlay = gameService.play(gameTypes,gameStrategy);

        // Then
        Assertions.assertEquals(BlackJackResults.SEVENTEEN, actualBankGamePlay);
        Assertions.assertEquals(BlackJackResults.EIGHTEEN, actualPlayerGamePlay);
    }

}
