package brocoeur.example.games.controller;

import brocoeur.example.common.AnalyticServiceRequestTypes;
import brocoeur.example.common.GameTypes;
import brocoeur.example.common.request.AnalyticServiceRequest;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.games.GamesConfigProperties;
import brocoeur.example.games.service.GameService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

import static brocoeur.example.common.GameStrategyTypes.ROULETTE_RISKY;
import static brocoeur.example.common.OfflineGameStrategyTypes.OFFLINE_COIN_TOSS_RANDOM;
import static brocoeur.example.common.ServiceRequestTypes.DIRECT;
import static brocoeur.example.common.ServiceRequestTypes.OFFLINE;
import static brocoeur.example.common.cointoss.CoinTossPlay.HEAD;
import static brocoeur.example.common.roulette.RoulettePlay.GREEN;
import static brocoeur.example.common.roulette.RoulettePlay.RED;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(MockitoExtension.class)
class GamesControllerTest {

    @Captor
    ArgumentCaptor<AnalyticServiceRequest> analyticServiceRequestCaptor;
    @Captor
    ArgumentCaptor<CorrelationData> correlationDataCaptor;

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
            var playerRequest = new PlayerRequest(userId, ROULETTE_RISKY, null, amountToGamble, linkedJobId);
            var serviceRequest = new ServiceRequest(DIRECT, playerRequest, null);

            Mockito.when(gameServiceMock.play(GameTypes.ROULETTE)).thenReturn(GREEN);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("analyticDirectExchange");
            Mockito.when(gamesConfigPropertiesMock.getRpcReplyMessageQueue()).thenReturn("analyticInput");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertAndSend(eq("analyticDirectExchange"), eq("analyticInput"), analyticServiceRequestCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var playerResponse = new PlayerResponse(123, 12345, true, amountToGamble, linkedJobId);
            var analyticServiceRequest = new AnalyticServiceRequest(AnalyticServiceRequestTypes.MONEY_MANAGEMENT, playerResponse);
            MatcherAssert.assertThat(analyticServiceRequestCaptor.getValue(), equalTo(analyticServiceRequest));
        }

        @Test
        void shouldProcessDirectMsgForLosingUser() {
            // Given
            var userId = "12345";
            var amountToGamble = 8;
            var linkedJobId = 123456;
            var playerRequest = new PlayerRequest(userId, ROULETTE_RISKY, null, amountToGamble, linkedJobId);
            var serviceRequest = new ServiceRequest(DIRECT, playerRequest, null);

            Mockito.when(gameServiceMock.play(GameTypes.ROULETTE)).thenReturn(RED);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("analyticDirectExchange");
            Mockito.when(gamesConfigPropertiesMock.getRpcReplyMessageQueue()).thenReturn("analyticInput");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertAndSend(eq("analyticDirectExchange"), eq("analyticInput"), analyticServiceRequestCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var playerResponse = new PlayerResponse(123, 12345, false, amountToGamble, linkedJobId);
            var analyticServiceRequest = new AnalyticServiceRequest(AnalyticServiceRequestTypes.MONEY_MANAGEMENT, playerResponse);
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
            var playerRequest = new PlayerRequest(userId, null, OFFLINE_COIN_TOSS_RANDOM, amountToGamble, linkedJobId);
            var serviceRequest = new ServiceRequest(OFFLINE, playerRequest, timeToLive);

            Mockito.when(gameServiceMock.play(GameTypes.COIN_TOSS))
                    .thenReturn(HEAD)
                    .thenReturn(HEAD)
                    .thenReturn(HEAD);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("analyticDirectExchange");
            Mockito.when(gamesConfigPropertiesMock.getRpcReplyMessageQueue()).thenReturn("analyticInput");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertAndSend(eq("analyticDirectExchange"), eq("analyticInput"), analyticServiceRequestCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var playerResponse = new PlayerResponse(324, 12345, List.of(true, false, true), amountToGamble, linkedJobId);
            var analyticServiceRequest = new AnalyticServiceRequest(AnalyticServiceRequestTypes.MONEY_MANAGEMENT, playerResponse);
            MatcherAssert.assertThat(analyticServiceRequestCaptor.getValue(), equalTo(analyticServiceRequest));
        }
    }
}
