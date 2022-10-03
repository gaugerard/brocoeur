package nerima.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static nerima.example.GameStrategyTypes.*;

@ExtendWith(MockitoExtension.class)
class NerimaServiceTest {

    @InjectMocks
    private NerimaService nerimaService;

    public static Stream<Arguments> getInputForRoulette() {
        return Stream.of(
                Arguments.of("Test - Roulette Risky", ROULETTE_RISKY, "12345 used strategy: RouletteRiskyStrategy and will play: GREEN"),
                Arguments.of("Test - Roulette Safe", ROULETTE_SAFE, "12345 used strategy: RouletteSafeStrategy and will play: RED")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getInputForRoulette")
    void shouldTestRouletteRiskyStrategy(final String testName, final GameStrategyTypes rouletteStrategyType, final String expected) {
        // Given
        var userId = "12345";
        var serviceRequest = new ServiceRequest(userId, rouletteStrategyType);

        // When
        var actual = nerimaService.play(serviceRequest);

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldTestCoinTossStrategy() {
        // Given
        var userId = "67890";
        var serviceRequest = new ServiceRequest(userId, COIN_TOSS_RANDOM);

        // When
        var actual = nerimaService.play(serviceRequest);

        // Then
        var expectedStart = "67890 used strategy: RandomCoinToss and will play: ";
        Assertions.assertTrue(actual.startsWith(expectedStart));
        Assertions.assertTrue(actual.endsWith("TAIL") || actual.endsWith("HEAD"));
    }
}
