package brocoeur.example.games.controller;

import brocoeur.example.broker.common.request.AnalyticServiceRequest;
import brocoeur.example.broker.common.request.ServiceRequest;
import brocoeur.example.broker.common.response.ServiceResponse;
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

import static brocoeur.example.broker.common.GameStrategyTypes.ROULETTE_RISKY;
import static brocoeur.example.broker.common.OfflineGameStrategyTypes.OFFLINE_COIN_TOSS_RANDOM;
import static brocoeur.example.broker.common.cointoss.CoinTossPlay.HEAD;
import static brocoeur.example.broker.common.roulette.RoulettePlay.GREEN;
import static brocoeur.example.broker.common.roulette.RoulettePlay.RED;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;


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
            var serviceRequest = new ServiceRequest(userId, ROULETTE_RISKY);

            Mockito.when(gameServiceMock.play(serviceRequest.getGameStrategyTypes().getGameTypes())).thenReturn(GREEN);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("exchange1");
            Mockito.when(gamesConfigPropertiesMock.getRpcReplyMessageQueue()).thenReturn("MyQ2");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertSendAndReceive(eq("exchange1"), eq("MyQ2"), serviceResponseCaptor.capture(), correlationDataCaptor.capture());
            Mockito.verify(rabbitTemplateMock).convertAndSend(eq("analyticDirectExchange"), eq("analyticInput"), analyticServiceRequestCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var serviceResponse = new ServiceResponse(userId, true);
            MatcherAssert.assertThat(serviceResponseCaptor.getValue(), equalTo(serviceResponse));
            var analyticServiceRequest = new AnalyticServiceRequest(123, 12345, true);
            MatcherAssert.assertThat(analyticServiceRequestCaptor.getValue(), equalTo(analyticServiceRequest));
            MatcherAssert.assertThat(correlationDataCaptor.getValue().getId(), equalTo(userId));
        }

        @Test
        void shouldProcessDirectMsgForLosingUser() {
            // Given
            var userId = "12345";
            var serviceRequest = new ServiceRequest(userId, ROULETTE_RISKY);

            Mockito.when(gameServiceMock.play(serviceRequest.getGameStrategyTypes().getGameTypes())).thenReturn(RED);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("exchange1");
            Mockito.when(gamesConfigPropertiesMock.getRpcReplyMessageQueue()).thenReturn("MyQ2");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertSendAndReceive(eq("exchange1"), eq("MyQ2"), serviceResponseCaptor.capture(), correlationDataCaptor.capture());
            Mockito.verify(rabbitTemplateMock).convertAndSend(eq("analyticDirectExchange"), eq("analyticInput"), analyticServiceRequestCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var serviceResponse = new ServiceResponse(userId, false);
            MatcherAssert.assertThat(serviceResponseCaptor.getValue(), equalTo(serviceResponse));
            var analyticServiceRequest = new AnalyticServiceRequest(123, 12345, false);
            MatcherAssert.assertThat(analyticServiceRequestCaptor.getValue(), equalTo(analyticServiceRequest));
            MatcherAssert.assertThat(correlationDataCaptor.getValue().getId(), equalTo(userId));
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
            var serviceRequest = new ServiceRequest(userId, OFFLINE_COIN_TOSS_RANDOM, timeToLive);

            Mockito.when(gameServiceMock.play(serviceRequest.getOfflineGameStrategyTypes().getGameTypes()))
                    .thenReturn(HEAD)
                    .thenReturn(HEAD)
                    .thenReturn(HEAD);
            Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("exchange1");
            Mockito.when(gamesConfigPropertiesMock.getRpcReplyMessageQueue()).thenReturn("MyQ2");

            // When
            gamesController.getMsg(serviceRequest);

            // Then
            Mockito.verify(rabbitTemplateMock).convertSendAndReceive(eq("exchange1"), eq("MyQ2"), serviceResponseCaptor.capture(), correlationDataCaptor.capture());
            Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

            var serviceResponseCaptorValue = serviceResponseCaptor.getValue();
            MatcherAssert.assertThat(serviceResponseCaptorValue.getUserId(), equalTo(userId));
            MatcherAssert.assertThat(serviceResponseCaptorValue.getListOfIsWinner().size(), equalTo(timeToLive));
            MatcherAssert.assertThat(correlationDataCaptor.getValue().getId(), equalTo(userId));
        }
    }
}
