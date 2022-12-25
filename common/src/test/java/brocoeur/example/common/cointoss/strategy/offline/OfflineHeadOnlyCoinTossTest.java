package brocoeur.example.common.cointoss.strategy.offline;

import brocoeur.example.common.cointoss.CoinTossPlay;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class OfflineHeadOnlyCoinTossTest {

    @InjectMocks
    private OfflineHeadOnlyCoinToss offlineHeadOnlyCoinToss;

    @Test
    void shouldGetHeadWithOfflineHeadOnlyCoinToss(){
        // Given - When
        var actualPlay = offlineHeadOnlyCoinToss.playOffline(Collections.emptyList());

        // Then
        Assertions.assertEquals(CoinTossPlay.HEAD, actualPlay);
    }
}
