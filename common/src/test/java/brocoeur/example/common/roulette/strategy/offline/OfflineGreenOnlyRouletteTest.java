package brocoeur.example.common.roulette.strategy.offline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static brocoeur.example.common.roulette.RoulettePlay.ZERO;

@ExtendWith(MockitoExtension.class)
class OfflineGreenOnlyRouletteTest {

    @InjectMocks
    private OfflineGreenOnlyRoulette offlineGreenOnlyRoulette;

    @Test
    void shouldGetGreenWithOfflineGreenOnlyRoulette(){
        // Given - When
        var actualPlay = offlineGreenOnlyRoulette.playOffline(Collections.emptyList());

        // Then
        Assertions.assertEquals(ZERO, actualPlay);
    }
}
