package brocoeur.example.games.service.cointoss;

import brocoeur.example.common.GameStrategyTypes;
import brocoeur.example.common.request.PlayerRequest;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

class CoinTossServiceTest {

    @Test
    void shouldPlayCoinToss() {
        // Given
        var coinTossService = new CoinTossService();

        var playerRequest = new PlayerRequest("8", GameStrategyTypes.COIN_TOSS_RANDOM, 50, 4654848);
        var ttl = 1;

        // When
        var listPlayerResponse = coinTossService.play(playerRequest, ttl);

        // Then
        MatcherAssert.assertThat(listPlayerResponse.size(), equalTo(1));
        MatcherAssert.assertThat(listPlayerResponse.get(0).getGameId(), equalTo(324));
        MatcherAssert.assertThat(listPlayerResponse.get(0).getUserId(), equalTo(8));
        MatcherAssert.assertThat(listPlayerResponse.get(0).getInitialAmount(), equalTo(50));
        MatcherAssert.assertThat(listPlayerResponse.get(0).getLinkedJobId(), equalTo(4654848));
    }
}
