package brocoeur.example.common.blackjack.strategy;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
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

import static brocoeur.example.common.blackjack.BlackJackPlay.*;


@ExtendWith(MockitoExtension.class)
class BlackJackRiskyTest {

    @InjectMocks
    private BlackJackRisky blackJackRisky;

    public static Stream<Arguments> getInputForBlackJackRisky() {
        return Stream.of(
                Arguments.of("Test - HIT 20 euro", 100, null, Collections.emptyList(), new Gamble(HIT, 20)),
                Arguments.of("Test - HIT all in (less than 20 euro)", 15, null, Collections.emptyList(), new Gamble(HIT, 15)),
                Arguments.of("Test - DOUBLE", 50, 20, List.of(new DeckOfCards.Card(5), new DeckOfCards.Card(7)), new Gamble(DOUBLE, 20)),
                Arguments.of("Test - STOP 0 euro", 100, null, List.of(new DeckOfCards.Card(5), new DeckOfCards.Card(12), new DeckOfCards.Card(5)), new Gamble(STOP, 0)),
                Arguments.of("Test - HIT 0 euro", 100, null, List.of(new DeckOfCards.Card(5)), new Gamble(HIT, 0))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getInputForBlackJackRisky")
    void shouldTestBlackJackRiskyStrategy(final String testName, final int availableMoney, final Integer initialBet, final List<DeckOfCards.Card> playerCards, final Gamble expectedGamble) {
        // Given - When
        var actualPlay = blackJackRisky.play(availableMoney, initialBet, playerCards);

        // Then
        Assertions.assertEquals(expectedGamble, actualPlay);
    }
}
