package brocoeur.example.nerima.strategy;

import brocoeur.example.nerima.service.roulette.strategy.direct.RouletteSafeStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static brocoeur.example.nerima.service.roulette.RoulettePlay.RED;

@ExtendWith(MockitoExtension.class)
class RouletteSafeStrategyTest {

    @InjectMocks
    private RouletteSafeStrategy rouletteSafeStrategy;

    @Test
    void shouldGetREDWithSafeRouletteStrategy() {
        // Given - When
        var actualPlay = rouletteSafeStrategy.getStrategyPlay();

        // Then
        Assertions.assertEquals(RED, actualPlay);
    }
}
