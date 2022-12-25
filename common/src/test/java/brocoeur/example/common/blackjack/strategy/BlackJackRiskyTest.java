package brocoeur.example.common.blackjack.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static brocoeur.example.common.blackjack.BlackJackPlay.MORE;

@ExtendWith(MockitoExtension.class)
class BlackJackRiskyTest {

    @InjectMocks
    private BlackJackRisky blackJackRisky;

    @Test
    void shouldGetStopAtTwentyRiskyRouletteStrategy() {
        // Given - When
        var actualPlay = blackJackRisky.play();

        // Then
        Assertions.assertEquals(MORE, actualPlay);
    }
}
