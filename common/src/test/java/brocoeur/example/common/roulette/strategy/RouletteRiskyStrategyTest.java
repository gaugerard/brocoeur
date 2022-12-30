package brocoeur.example.common.roulette.strategy;

import brocoeur.example.common.Gamble;
import brocoeur.example.common.roulette.RoulettePlay;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static brocoeur.example.common.roulette.RoulettePlay.*;

@ExtendWith(MockitoExtension.class)
class RouletteRiskyStrategyTest {

    @InjectMocks
    private RouletteRiskyStrategy rouletteRiskyStrategy;

    public static Stream<Arguments> getInputForRouletteRiskyStrategy() {
        return Stream.of(
                Arguments.of("Test - ZERO 2 euro", 100, List.of(FIFTEEN, ZERO), List.of(new Gamble(ZERO, 2))),
                Arguments.of("Test - BLACK 10 euro", 100, List.of(SIXTEEN, THREE, THIRTY_SIX), List.of(new Gamble(BLACK, 10))),
                Arguments.of("Test - RED 10 euro", 100, List.of(ZERO, FIFTEEN, TWENTY_NINE, TWENTY_FOUR), List.of(new Gamble(ZERO, 2), new Gamble(RED, 10)))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getInputForRouletteRiskyStrategy")
    void shouldTestRouletteRiskyStrategy(final String testName, final int availableMoney, final List<RoulettePlay> previousRoulettePlay, final List<Gamble> expectedListOfGamble) {
        // Given - When
        var actualPlay = rouletteRiskyStrategy.play(availableMoney, previousRoulettePlay);

        // Then
        Assertions.assertEquals(expectedListOfGamble, actualPlay);
    }

    @Test
    void shouldTestRandom2NumberForRouletteRiskyStrategy() {
        // Given - When
        var actualPlay = rouletteRiskyStrategy.play(80, Collections.emptyList());

        // Then
        Assertions.assertEquals(2, actualPlay.size());
    }
}
