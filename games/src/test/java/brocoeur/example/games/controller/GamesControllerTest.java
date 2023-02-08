package brocoeur.example.games.controller;

import brocoeur.example.common.request.AnalyticServiceRequest;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.games.GamesConfigProperties;
import brocoeur.example.games.service.GameService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

import static brocoeur.example.common.AnalyticServiceRequestTypes.MONEY_MANAGEMENT;
import static brocoeur.example.common.GameStrategyTypes.*;
import static brocoeur.example.common.ServiceRequestTypes.MULTIPLAYER;
import static brocoeur.example.common.ServiceRequestTypes.SINGLE_PLAYER;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(MockitoExtension.class)
class GamesControllerTest {

    @Captor
    ArgumentCaptor<AnalyticServiceRequest> analyticServiceRequestCaptor;

    @Mock
    private GameService gameServiceMock;
    @Mock
    private GamesConfigProperties gamesConfigPropertiesMock;
    @Mock
    private RabbitTemplate rabbitTemplateMock;
    @InjectMocks
    private GamesController gamesController;

    @Nested
    @DisplayName("Tests for DIRECT service Request")
    class DirectGamesControllerTest {
        @Test
        void shouldProcessDirectMsgForWinningUser() {
            // Given
            var userId = "12345";
            var amountToGamble = 5;
            var linkedJobId = 123456;
            var playerRequest = new PlayerRequest(userId, ROULETTE_RISKY, amountToGamble, linkedJobId);
            var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, 1);
            var playerResponseList = List.of(new PlayerResponse(123, 12345, 100, amountToGamble, linkedJobId));

            Mockito.when(gameServiceMock.playGame(serviceRequest)).thenReturn(playerResponseList);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("analyticDirectExchange");
            Mockito.when(gamesConfigPropertiesMock.getAnalyticInputQueueName()).thenReturn("analyticInputQueue");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertAndSend(eq("analyticDirectExchange"), eq("analyticInputQueue"), analyticServiceRequestCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var playerResponse = new PlayerResponse(123, 12345, 100, amountToGamble, linkedJobId);
            var analyticServiceRequest = new AnalyticServiceRequest(MONEY_MANAGEMENT, playerResponse);
            MatcherAssert.assertThat(analyticServiceRequestCaptor.getValue(), equalTo(analyticServiceRequest));
        }

        @Test
        void shouldProcessDirectMsgForLosingUser() {
            // Given
            var userId = "12345";
            var amountToGamble = 8;
            var linkedJobId = 123456;
            var playerRequest = new PlayerRequest(userId, ROULETTE_RISKY, amountToGamble, linkedJobId);
            var serviceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, 1);
            var playerResponseList = List.of(new PlayerResponse(123, 12345, 100, amountToGamble, linkedJobId));

            Mockito.when(gameServiceMock.playGame(serviceRequest)).thenReturn(playerResponseList);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("analyticDirectExchange");
            Mockito.when(gamesConfigPropertiesMock.getAnalyticInputQueueName()).thenReturn("analyticInputQueue");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertAndSend(eq("analyticDirectExchange"), eq("analyticInputQueue"), analyticServiceRequestCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var playerResponse = new PlayerResponse(123, 12345, 100, amountToGamble, linkedJobId);
            var analyticServiceRequest = new AnalyticServiceRequest(MONEY_MANAGEMENT, playerResponse);
            MatcherAssert.assertThat(analyticServiceRequestCaptor.getValue(), equalTo(analyticServiceRequest));
        }
    }

    @Nested
    @DisplayName("Tests for OFFLINE service Request")
    class OfflineGamesControllerTest {
        @Test
        void shouldProcessOfflineMsgForLosingUser() {
            // Given
            var userId = "12345";
            var timeToLive = 3;
            var amountToGamble = 8;
            var linkedJobId = 123456;
            var playerRequest = new PlayerRequest(userId, OFFLINE_COIN_TOSS_RANDOM, amountToGamble, linkedJobId);
            var offlineServiceRequest = new ServiceRequest(SINGLE_PLAYER, playerRequest, timeToLive);
            var playerResponseList1 = List.of(new PlayerResponse(324, 12345, 100, amountToGamble, linkedJobId));

            Mockito.when(gameServiceMock.playGame(offlineServiceRequest)).thenReturn(playerResponseList1);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("analyticDirectExchange");
            Mockito.when(gamesConfigPropertiesMock.getAnalyticInputQueueName()).thenReturn("analyticInputQueue");

            // When
            gamesController.getMsg(offlineServiceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertAndSend(eq("analyticDirectExchange"), eq("analyticInputQueue"), analyticServiceRequestCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var playerResponse = new PlayerResponse(324, 12345, 100, amountToGamble, linkedJobId);
            var analyticServiceRequest = new AnalyticServiceRequest(MONEY_MANAGEMENT, playerResponse);
            MatcherAssert.assertThat(analyticServiceRequestCaptor.getValue(), equalTo(analyticServiceRequest));
        }
    }

    @Nested
    @DisplayName("Tests for MULTIPLAYER service Request")
    class MultiplayerGamesControllerTest {
        @Test
        void shouldProcessMultiplayerMsg() {
            // Given
            var playerRequest1 = new PlayerRequest("8", POKER_RANDOM, 20, 1);
            var playerRequest2 = new PlayerRequest("9", POKER_RANDOM, 25, 2);
            var playerRequest3 = new PlayerRequest("10", POKER_RANDOM, 30, 3);
            var serviceRequest = new ServiceRequest(MULTIPLAYER, List.of(playerRequest1, playerRequest2, playerRequest3), null);

            var playerResponse1 = new PlayerResponse(420, 8, 100, 20, 1);
            var playerResponse2 = new PlayerResponse(420, 9, 100, 25, 2);
            var playerResponse3 = new PlayerResponse(420, 10, 100, 30, 3);

            Mockito.when(gameServiceMock.playMultiplayerGame(serviceRequest)).thenReturn(List.of(playerResponse1, playerResponse2, playerResponse3));
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("analyticDirectExchange");
            Mockito.when(gamesConfigPropertiesMock.getAnalyticInputQueueName()).thenReturn("analyticInputQueue");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            var expectedAnalyticServiceRequest = new AnalyticServiceRequest(MONEY_MANAGEMENT, List.of(playerResponse1, playerResponse2, playerResponse3));
            Mockito.verify(rabbitTemplateMock).convertAndSend("analyticDirectExchange", "analyticInputQueue", expectedAnalyticServiceRequest);
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);
        }

        @Test
        void shouldNotProcessMultiplayerMsgWhenPlayerCountIsLowerThan3() {
            // Given
            var playerRequest1 = new PlayerRequest("8", POKER_RANDOM, 20, 1);
            var playerRequest2 = new PlayerRequest("9", POKER_RANDOM, 25, 2);
            var serviceRequest = new ServiceRequest(MULTIPLAYER, List.of(playerRequest1, playerRequest2), null);

            // When
            var exception = Assertions.assertThrows(IllegalStateException.class, () -> gamesController.getMsg(serviceRequest));

            Assertions.assertEquals("ServiceRequest.PlayerRequestList must be of size 3 (for Poker).", exception.getMessage());

            // Then
            Mockito.verifyNoInteractions(gameServiceMock);
            Mockito.verifyNoInteractions(gamesConfigPropertiesMock);
            Mockito.verifyNoInteractions(rabbitTemplateMock);
        }
    }
}
