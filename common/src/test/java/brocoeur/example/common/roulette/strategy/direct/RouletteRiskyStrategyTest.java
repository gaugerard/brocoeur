package brocoeur.example.common.roulette.strategy.direct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static brocoeur.example.common.roulette.RoulettePlay.GREEN;

@ExtendWith(MockitoExtension.class)
class RouletteRiskyStrategyTest {

    @InjectMocks
    private RouletteRiskyStrategy rouletteRiskyStrategy;

    @Test
    void shouldGetGreenWithRiskyRouletteStrategy() {
        // Given - When
        var actualPlay = rouletteRiskyStrategy.getStrategyPlay();

        // Then
        Assertions.assertEquals(GREEN, actualPlay);
    }
}
