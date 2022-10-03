package nerima.roulette.strategy;

import nerima.example.roulette.strategy.RouletteSafeStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static nerima.example.roulette.RoulettePlay.RED;

@ExtendWith(MockitoExtension.class)
class RouletteSafeStrategyTest {

    @InjectMocks
    private RouletteSafeStrategy rouletteSafeStrategy;

    @Test
    void shouldGetREDWithSafeRouletteStrategy() {
        // Given - When
        var actualPlay = rouletteSafeStrategy.play();

        // Then
        Assertions.assertEquals(RED, actualPlay);
    }
}
