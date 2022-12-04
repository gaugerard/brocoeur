package brocoeur.example.games.controller;

import brocoeur.example.common.AnalyticServiceRequestTypes;
import brocoeur.example.common.request.AnalyticServiceRequest;
import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.common.response.ServiceResponse;
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
import static brocoeur.example.common.cointoss.CoinTossPlay.HEAD;
import static brocoeur.example.common.roulette.RoulettePlay.GREEN;
import static brocoeur.example.common.roulette.RoulettePlay.RED;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(MockitoExtension.class)
class GamesControllerTest {

    @Captor
    ArgumentCaptor<ServiceResponse> serviceResponseCaptor;
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
            var serviceRequest = new ServiceRequest(userId, ROULETTE_RISKY, amountToGamble, linkedJobId);

            Mockito.when(gameServiceMock.play(serviceRequest.getGameStrategyTypes().getGameTypes())).thenReturn(GREEN);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("analyticDirectExchange");
            Mockito.when(gamesConfigPropertiesMock.getRpcReplyMessageQueue()).thenReturn("analyticInput");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertAndSend(eq("analyticDirectExchange"), eq("analyticInput"), analyticServiceRequestCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var analyticServiceRequest = new AnalyticServiceRequest(AnalyticServiceRequestTypes.MONEY_MANAGEMENT, 123, 12345, true, amountToGamble, linkedJobId);
            MatcherAssert.assertThat(analyticServiceRequestCaptor.getValue(), equalTo(analyticServiceRequest));
        }

        @Test
        void shouldProcessDirectMsgForLosingUser() {
            // Given
            var userId = "12345";
            var amountToGamble = 8;
            var linkedJobId = 123456;
            var serviceRequest = new ServiceRequest(userId, ROULETTE_RISKY, amountToGamble, linkedJobId);

            Mockito.when(gameServiceMock.play(serviceRequest.getGameStrategyTypes().getGameTypes())).thenReturn(RED);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("analyticDirectExchange");
            Mockito.when(gamesConfigPropertiesMock.getRpcReplyMessageQueue()).thenReturn("analyticInput");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertAndSend(eq("analyticDirectExchange"), eq("analyticInput"), analyticServiceRequestCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var analyticServiceRequest = new AnalyticServiceRequest(AnalyticServiceRequestTypes.MONEY_MANAGEMENT, 123, 12345, false, amountToGamble, linkedJobId);
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
            var serviceRequest = new ServiceRequest(userId, OFFLINE_COIN_TOSS_RANDOM, timeToLive, amountToGamble, linkedJobId);

            Mockito.when(gameServiceMock.play(serviceRequest.getOfflineGameStrategyTypes().getGameTypes()))
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

            var analyticServiceRequest = new AnalyticServiceRequest(AnalyticServiceRequestTypes.MONEY_MANAGEMENT, 324, 12345, List.of(true, false, true), amountToGamble, linkedJobId);
            MatcherAssert.assertThat(analyticServiceRequestCaptor.getValue(), equalTo(analyticServiceRequest));
        }
    }
}
