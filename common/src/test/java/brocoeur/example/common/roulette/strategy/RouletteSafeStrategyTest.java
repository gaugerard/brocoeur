package brocoeur.example.common.roulette.strategy;

import brocoeur.example.common.Gamble;
import brocoeur.example.common.roulette.RoulettePlay;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static brocoeur.example.common.roulette.RoulettePlay.RED;
import static brocoeur.example.common.roulette.RoulettePlay.ZERO;

@ExtendWith(MockitoExtension.class)
class RouletteSafeStrategyTest {

    @InjectMocks
    private RouletteSafeStrategy rouletteSafeStrategy;

    public static Stream<Arguments> getInputForRouletteSafeStrategy() {
        return Stream.of(
                Arguments.of("Test - RED 5 euro, ZERO 5 euro", 100, List.of(ZERO), List.of(new Gamble(RED, 5), new Gamble(ZERO, 5))),
                Arguments.of("Test - RED 5 euro, ZERO 1 euro", 100, Collections.emptyList(), List.of(new Gamble(RED, 5), new Gamble(ZERO, 1)))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getInputForRouletteSafeStrategy")
    void shouldTestRouletteRiskyStrategy(final String testName, final int availableMoney, final List<RoulettePlay> previousRoulettePlay, final List<Gamble> expectedListOfGamble) {
        // Given - When
        var actualPlay = rouletteSafeStrategy.play(availableMoney, previousRoulettePlay);

        // Then
        Assertions.assertEquals(expectedListOfGamble, actualPlay);
    }
}
