package brocoeur.example.nerima.controller;

import brocoeur.example.nerima.NerimaConfigProperties;
import brocoeur.example.nerima.service.GameStrategyTypes;
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

import static brocoeur.example.nerima.service.GameStrategyTypes.*;
import static brocoeur.example.nerima.service.OfflineGameStrategyTypes.OFFLINE_COIN_TOSS_RANDOM;

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
        var serviceRequest = new ServiceRequest(userId, rouletteStrategyType);
        var serviceResponse = new ServiceResponse(userId, true);

        Mockito.when(nerimaConfigPropertiesMock.getRpcExchange()).thenReturn("myexchange1");
        Mockito.when(nerimaConfigPropertiesMock.getRpcMessageQueue()).thenReturn("MyQ1");
        Mockito.when(rabbitTemplateMock.convertSendAndReceive("myexchange1", "MyQ1", serviceRequest)).thenReturn(serviceResponse);

        // When
        var actualServiceResponse = nerimaController.postDirectPlay(serviceRequest);

        // Then
        Assertions.assertEquals(new ResponseEntity<>(serviceResponse, HttpStatus.OK), actualServiceResponse);
    }

    @Test
    void shouldPostOfflinePlayAndGetHttpStatusOK() {
        // Given
        var userId = "12345";
        var timeToLive = 3;
        var serviceRequest = new ServiceRequest(userId, OFFLINE_COIN_TOSS_RANDOM, timeToLive);
        var serviceResponse = new ServiceResponse(userId, List.of(true, false, false));

        Mockito.when(nerimaConfigPropertiesMock.getRpcExchange()).thenReturn("myexchange1");
        Mockito.when(nerimaConfigPropertiesMock.getRpcMessageQueue()).thenReturn("MyQ1");
        Mockito.when(rabbitTemplateMock.convertSendAndReceive("myexchange1", "MyQ1", serviceRequest)).thenReturn(serviceResponse);

        // When
        var actualServiceResponse = nerimaController.postOfflinePlay(serviceRequest);

        // Then
        Assertions.assertEquals(new ResponseEntity<>(serviceResponse, HttpStatus.OK), actualServiceResponse);
    }
}
