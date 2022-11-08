package brocoeur.example.games.controller;

import brocoeur.example.games.GamesConfigProperties;
import brocoeur.example.games.service.GameService;
import brocoeur.example.nerima.controller.ServiceRequest;
import brocoeur.example.nerima.controller.ServiceResponse;
import brocoeur.example.nerima.service.roulette.RoulettePlay;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static brocoeur.example.nerima.service.GameStrategyTypes.ROULETTE_RISKY;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
class GamesControllerTest {

    @Captor
    ArgumentCaptor<ServiceResponse> serviceResponseCaptor;
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

    @Test
    void shouldTestGetMsgForWinningUser() {
        // Given
        var userId = "12345";
        var serviceRequest = new ServiceRequest(userId, ROULETTE_RISKY);

        Mockito.when(gameServiceMock.play(serviceRequest.getGameStrategyTypes().getGameTypes())).thenReturn(RoulettePlay.GREEN);
        Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("exchange1");
        Mockito.when(gamesConfigPropertiesMock.getRpcReplyMessageQueue()).thenReturn("MyQ2");

        // When
        gamesController.getMsg(serviceRequest);

        // Then
        Mockito.verify(rabbitTemplateMock).convertSendAndReceive(eq("exchange1"), eq("MyQ2"), serviceResponseCaptor.capture(), correlationDataCaptor.capture());
        Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

        var serviceResponse = new ServiceResponse(userId, true);
        MatcherAssert.assertThat(serviceResponseCaptor.getValue(), equalTo(serviceResponse));
        MatcherAssert.assertThat(correlationDataCaptor.getValue().getId(), equalTo(userId));
    }

    @Test
    void shouldTestGetMsgForLosingUser() {
        // Given
        var userId = "12345";
        var serviceRequest = new ServiceRequest(userId, ROULETTE_RISKY);

        Mockito.when(gameServiceMock.play(serviceRequest.getGameStrategyTypes().getGameTypes())).thenReturn(RoulettePlay.RED);
        Mockito.when(gamesConfigPropertiesMock.getRpcExchange()).thenReturn("exchange1");
        Mockito.when(gamesConfigPropertiesMock.getRpcReplyMessageQueue()).thenReturn("MyQ2");

        // When
        gamesController.getMsg(serviceRequest);

        // Then
        Mockito.verify(rabbitTemplateMock).convertSendAndReceive(eq("exchange1"), eq("MyQ2"), serviceResponseCaptor.capture(), correlationDataCaptor.capture());
        Mockito.verifyNoMoreInteractions(rabbitTemplateMock);

        var serviceResponse = new ServiceResponse(userId, false);
        MatcherAssert.assertThat(serviceResponseCaptor.getValue(), equalTo(serviceResponse));
        MatcherAssert.assertThat(correlationDataCaptor.getValue().getId(), equalTo(userId));
    }
}
