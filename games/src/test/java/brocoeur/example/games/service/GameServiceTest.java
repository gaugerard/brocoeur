package brocoeur.example.games.service;

import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.games.service.blackjack.BlackJackService;
import brocoeur.example.games.service.cointoss.CoinTossService;
import brocoeur.example.games.service.poker.PokerService;
import brocoeur.example.games.service.roulette.RouletteService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static brocoeur.example.common.GameStrategyTypes.*;
import static brocoeur.example.common.ServiceRequestTypes.MULTIPLAYER;
import static brocoeur.example.common.ServiceRequestTypes.SINGLE_PLAYER;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private CoinTossService coinTossServiceMock;
    @Mock
    private RouletteService rouletteServiceMock;
    @Mock
    private BlackJackService blackJackServiceMock;
    @Mock
    private PokerService pokerServiceMock;

    @InjectMocks
    private GameService gameService;

    @Nested
    @DisplayName("Tests for DIRECT service Request")
    class DirectGameServiceTest {
        @Test
        void shouldTestPlayDirectGameRoulette() {
            // Given
            var playerRequest = new PlayerRequest("8", ROULETTE_RISKY, 25, 1);
            var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, 1);
            var playerResponseList = List.of(new PlayerResponse(123, 8, 100, 25, 1));

            when(rouletteServiceMock.play(playerRequest, 1)).thenReturn(playerResponseList);

            // When
            var actualPlayerResponseList = gameService.playGame(serviceRequest);

            // Then
            var expectedPlayerResponseList = List.of(new PlayerResponse(123, 8, 100, 25, 1));
            MatcherAssert.assertThat(actualPlayerResponseList, equalTo(expectedPlayerResponseList));
            Mockito.verifyNoMoreInteractions(rouletteServiceMock);
            Mockito.verifyNoInteractions(coinTossServiceMock);
            Mockito.verifyNoInteractions(blackJackServiceMock);
            Mockito.verifyNoInteractions(pokerServiceMock);
        }

        @Test
        void shouldTestPlayDirectGameCoinToss() {
            // Given
            var playerRequest = new PlayerRequest("8", COIN_TOSS_RANDOM, 35, 1);
            var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, 1);
            var playerResponseList = List.of(new PlayerResponse(123, 8, 100, 35, 1));

            when(coinTossServiceMock.play(playerRequest, 1)).thenReturn(playerResponseList);

            // When
            var actualPlayerResponseList = gameService.playGame(serviceRequest);

            // Then
            var expectedPlayerResponseList = List.of(new PlayerResponse(123, 8, 100, 35, 1));
            MatcherAssert.assertThat(actualPlayerResponseList, equalTo(expectedPlayerResponseList));
            Mockito.verifyNoMoreInteractions(coinTossServiceMock);
            Mockito.verifyNoInteractions(rouletteServiceMock);
            Mockito.verifyNoInteractions(blackJackServiceMock);
            Mockito.verifyNoInteractions(pokerServiceMock);
        }

        @Test
        void shouldTestPlayDirectGameBlackJack() {
            // Given
            var playerRequest = new PlayerRequest("8", BLACK_JACK_SAFE, 50, 1);
            var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, 1);
            var playerResponseList = List.of(new PlayerResponse(123, 8, 100, 50, 1));

            when(blackJackServiceMock.playBlackJackGame(playerRequest, 1)).thenReturn(playerResponseList);

            // When
            var actualPlayerResponseList = gameService.playGame(serviceRequest);

            // Then
            var expectedPlayerResponseList = List.of(new PlayerResponse(123, 8, 100, 50, 1));
            MatcherAssert.assertThat(actualPlayerResponseList, equalTo(expectedPlayerResponseList));
            Mockito.verifyNoMoreInteractions(blackJackServiceMock);
            Mockito.verifyNoInteractions(rouletteServiceMock);
            Mockito.verifyNoInteractions(coinTossServiceMock);
            Mockito.verifyNoInteractions(pokerServiceMock);
        }

        @Test
        void shouldTestPlayDirectGamePoker() {
            // Given
            var playerRequest = new PlayerRequest("8", POKER_RANDOM, 60, 1);
            var serviceRequest = new ServiceRequest(MULTIPLAYER, playerRequest, null);

            // When
            var exception = Assertions.assertThrows(IllegalStateException.class, () -> gameService.playGame(serviceRequest));

            Assertions.assertEquals("Poker should not be managed here.", exception.getMessage());

            // Then
            Mockito.verifyNoInteractions(coinTossServiceMock);
            Mockito.verifyNoInteractions(rouletteServiceMock);
            Mockito.verifyNoInteractions(blackJackServiceMock);
            Mockito.verifyNoInteractions(pokerServiceMock);
        }

        @Test
        void shouldTestPlayDirectGameForMissingMandatoryElement() {
            // Given
            var playerRequest = new PlayerRequest("8", null, 60, 1);
            var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, null);

            // When
            var exception = Assertions.assertThrows(IllegalStateException.class, () -> gameService.playGame(serviceRequest));

            Assertions.assertEquals("Missing mandatory 'GameStrategyTypes'.", exception.getMessage());

            // Then
            Mockito.verifyNoInteractions(coinTossServiceMock);
            Mockito.verifyNoInteractions(rouletteServiceMock);
            Mockito.verifyNoInteractions(blackJackServiceMock);
            Mockito.verifyNoInteractions(pokerServiceMock);
        }
    }

    @Nested
    @DisplayName("Tests for OFFLINE service Request")
    class OfflineGameServiceTest {

        @Test
        void shouldTestPlayOfflineGameCoinToss() {
            // Given
            var playerRequest = new PlayerRequest("8", OFFLINE_COIN_TOSS_HEAD_ONLY, 35, 1);
            var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, 1);
            List<Boolean> listOfIsWinner = List.of();
            var playerResponseList = List.of(new PlayerResponse(123, 8, 100, 35, 1));

            when(coinTossServiceMock.play(playerRequest, 1)).thenReturn(playerResponseList);

            // When
            var actualPlayerResponseList = gameService.playGame(serviceRequest);

            // Then
            var expectedPlayerResponseList = List.of(new PlayerResponse(123, 8, 100, 35, 1));
            MatcherAssert.assertThat(actualPlayerResponseList, equalTo(expectedPlayerResponseList));
            Mockito.verifyNoMoreInteractions(coinTossServiceMock);
            Mockito.verifyNoInteractions(rouletteServiceMock);
            Mockito.verifyNoInteractions(blackJackServiceMock);
            Mockito.verifyNoInteractions(pokerServiceMock);
        }


        @Test
        void shouldTestPlayOfflineGameForMissingMandatoryElement() {
            // Given
            var playerRequest = new PlayerRequest("8", null, 70, 1);
            var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, null);
            List<Boolean> listOfIsWinner = List.of();

            // When
            var exception = Assertions.assertThrows(IllegalStateException.class, () -> gameService.playGame(serviceRequest));

            Assertions.assertEquals("Missing mandatory 'GameStrategyTypes'.", exception.getMessage());

            // Then
            Mockito.verifyNoInteractions(coinTossServiceMock);
            Mockito.verifyNoInteractions(rouletteServiceMock);
            Mockito.verifyNoInteractions(blackJackServiceMock);
            Mockito.verifyNoInteractions(pokerServiceMock);
        }
    }

    @Nested
    @DisplayName("Tests for MULTIPLAYER service Request")
    class MultiplayerGameServiceTest {
        @Test
        void shouldTestPlayMultiplayerGameRoulette() {
            // Given
            var playerRequest1 = new PlayerRequest("8", POKER_RANDOM, 43, 1);
            var playerRequest2 = new PlayerRequest("9", POKER_RANDOM, 43, 2);
            var playerRequest3 = new PlayerRequest("10", POKER_RANDOM, 43, 3);
            var serviceRequest = new ServiceRequest(MULTIPLAYER, List.of(playerRequest1, playerRequest2, playerRequest3), null);
            var playerResponse1 = new PlayerResponse(420, 8, 100, 43, 1);
            var playerResponse2 = new PlayerResponse(420, 9, 100, 43, 2);
            var playerResponse3 = new PlayerResponse(420, 10, 100, 43, 3);
            var playerResponseList = List.of(playerResponse1, playerResponse2, playerResponse3);

            when(pokerServiceMock.playPokerGame(serviceRequest)).thenReturn(playerResponseList);

            // When
            var actualPlayerResponseList = gameService.playMultiplayerGame(serviceRequest);

            // Then
            var expectedPlayerResponse1 = new PlayerResponse(420, 8, 100, 43, 1);
            var expectedPlayerResponse2 = new PlayerResponse(420, 9, 100, 43, 2);
            var expectedPlayerResponse3 = new PlayerResponse(420, 10, 100, 43, 3);
            var expectedPlayerResponseList = List.of(expectedPlayerResponse1, expectedPlayerResponse2, expectedPlayerResponse3);
            MatcherAssert.assertThat(actualPlayerResponseList, equalTo(expectedPlayerResponseList));
            Mockito.verifyNoMoreInteractions(pokerServiceMock);
            Mockito.verifyNoInteractions(coinTossServiceMock);
            Mockito.verifyNoInteractions(blackJackServiceMock);
            Mockito.verifyNoInteractions(rouletteServiceMock);
        }

        @Test
        void shouldTestPlayMultiplayerGameForMissingMandatoryElement() {
            // Given
            var playerRequest = new PlayerRequest("8", null, 60, 1);
            var serviceRequest = new ServiceRequest(MULTIPLAYER, playerRequest, null);

            // When
            var exception = Assertions.assertThrows(IllegalStateException.class, () -> gameService.playMultiplayerGame(serviceRequest));

            Assertions.assertEquals("Missing mandatory 'GameStrategyTypes'.", exception.getMessage());

            // Then
            Mockito.verifyNoInteractions(coinTossServiceMock);
            Mockito.verifyNoInteractions(rouletteServiceMock);
            Mockito.verifyNoInteractions(blackJackServiceMock);
            Mockito.verifyNoInteractions(pokerServiceMock);
        }
    }
}
