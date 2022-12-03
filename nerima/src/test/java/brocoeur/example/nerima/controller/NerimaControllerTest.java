package brocoeur.example.nerima.controller;

import brocoeur.example.broker.common.GameStrategyTypes;
import brocoeur.example.broker.common.request.ServiceRequest;
import brocoeur.example.broker.common.response.ServiceResponse;
import brocoeur.example.nerima.NerimaConfigProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

import static brocoeur.example.broker.common.GameStrategyTypes.*;
import static brocoeur.example.broker.common.OfflineGameStrategyTypes.OFFLINE_COIN_TOSS_RANDOM;

@ExtendWith(MockitoExtension.class)
class NerimaControllerTest {

    @Mock
    private RabbitTemplate rabbitTemplateMock;
    @Mock
    private NerimaConfigProperties nerimaConfigPropertiesMock;

    @InjectMocks
    private NerimaController nerimaController;

    public static Stream<Arguments> getInputForRoulette() {
        return Stream.of(
                Arguments.of("Test - Roulette Risky", ROULETTE_RISKY),
                Arguments.of("Test - Roulette Safe", ROULETTE_SAFE),
                Arguments.of("Test - Coin Toss Random", COIN_TOSS_RANDOM)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getInputForRoulette")
    void shouldPostDirectPlayAndGetHttpStatusOK(final String testName, final GameStrategyTypes rouletteStrategyType) {
        // Given
        var userId = "12345";
        var serviceRequest = new ServiceRequest(userId, rouletteStrategyType, 5, 123456);

        Mockito.when(nerimaConfigPropertiesMock.getRpcExchange()).thenReturn("A1DirectExchange");
        Mockito.when(nerimaConfigPropertiesMock.getRpcMessageQueue()).thenReturn("MyA1");

        // When
        var actualServiceResponse = nerimaController.postDirectGamblePlay(serviceRequest);

        // Then
        Mockito.verify(rabbitTemplateMock).convertAndSend("A1DirectExchange", "MyA1", serviceRequest);
        Assertions.assertEquals(new ResponseEntity<>(serviceRequest, HttpStatus.OK), actualServiceResponse);
    }

    @Test
    void shouldPostOfflinePlayAndGetHttpStatusOK() {
        // Given
        var userId = "12345";
        var timeToLive = 3;
        var serviceRequest = new ServiceRequest(userId, OFFLINE_COIN_TOSS_RANDOM, timeToLive);

        Mockito.when(nerimaConfigPropertiesMock.getRpcExchange()).thenReturn("A1DirectExchange");
        Mockito.when(nerimaConfigPropertiesMock.getRpcMessageQueue()).thenReturn("MyA1");

        // When
        var actualServiceResponse = nerimaController.postOfflineGamblePlay(serviceRequest);

        // Then
        Mockito.verify(rabbitTemplateMock).convertAndSend("A1DirectExchange", "MyA1", serviceRequest);
        Assertions.assertEquals(new ResponseEntity<>(serviceRequest, HttpStatus.OK), actualServiceResponse);
    }
}
