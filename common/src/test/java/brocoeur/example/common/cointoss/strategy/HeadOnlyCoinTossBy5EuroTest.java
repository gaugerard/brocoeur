package brocoeur.example.common.cointoss.strategy;

import brocoeur.example.common.Gamble;
import brocoeur.example.common.cointoss.CoinTossPlay;
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

import static brocoeur.example.common.cointoss.CoinTossPlay.HEAD;
import static brocoeur.example.common.cointoss.CoinTossPlay.TAIL;

@ExtendWith(MockitoExtension.class)
class HeadOnlyCoinTossBy5EuroTest {

    @InjectMocks
    private HeadOnlyCoinTossBy5Euro headOnlyCoinTossBy5Euro;

    public static Stream<Arguments> getInputForHeadOnlyCoinTossBy5Euro() {
        return Stream.of(
                Arguments.of("Test - HEAD 5 euro", 25, Collections.emptyList(), new Gamble(HEAD, 5)),
                Arguments.of("Test - HEAD 5 euro", 25, List.of(HEAD, HEAD, TAIL), new Gamble(HEAD, 5)),
                Arguments.of("Test - HEAD all in", 4, Collections.emptyList(), new Gamble(HEAD, 4))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getInputForHeadOnlyCoinTossBy5Euro")
    void shouldTestHeadOnlyCoinTossBy5Euro(final String testName, final int availableMoney, final List<CoinTossPlay> previousRoulettePlay, final Gamble expectedGamble) {
        // Given - When
        var actualPlay = headOnlyCoinTossBy5Euro.play(availableMoney, previousRoulettePlay);

        // Then
        Assertions.assertEquals(expectedGamble, actualPlay);
    }
}
