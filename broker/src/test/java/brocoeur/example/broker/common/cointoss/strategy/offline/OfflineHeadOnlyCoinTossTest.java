package brocoeur.example.broker.common.cointoss.strategy.offline;

import brocoeur.example.broker.common.roulette.strategy.direct.RouletteRiskyStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static brocoeur.example.broker.common.cointoss.CoinTossPlay.HEAD;

@ExtendWith(MockitoExtension.class)
class OfflineHeadOnlyCoinTossTest {

    @InjectMocks
    private OfflineHeadOnlyCoinToss offlineHeadOnlyCoinToss;

    @Test
    void shouldGetHeadWithOfflineHeadOnlyCoinToss(){
        // Given - When
        var actualPlay = offlineHeadOnlyCoinToss.getOfflineStrategyPlay(Collections.emptyList());

        // Then
        Assertions.assertEquals(HEAD, actualPlay);
    }
}
