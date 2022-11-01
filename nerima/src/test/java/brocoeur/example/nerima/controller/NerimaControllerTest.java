package brocoeur.example.nerima.controller;

import brocoeur.example.nerima.service.GameStrategyTypes;
import org.junit.jupiter.api.Assertions;
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

import java.util.stream.Stream;

import static brocoeur.example.nerima.service.GameStrategyTypes.*;

@ExtendWith(MockitoExtension.class)
class NerimaControllerTest {

    @Mock
    private RabbitTemplate rabbitTemplateMock;
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
    void shouldConvertAndSendServiceRequestToQueueAndReturnResponseOK(final String testName, final GameStrategyTypes rouletteStrategyType) {
        // Given
        var userId = "12345";
        var serviceRequest = new ServiceRequest(userId, rouletteStrategyType);

        // When
        var actual = nerimaController.postPlay(serviceRequest);

        // Then
        Mockito.verify(rabbitTemplateMock).convertSendAndReceive("myexchange1", "", serviceRequest);
        Assertions.assertEquals(new ResponseEntity<>(serviceRequest, HttpStatus.OK), actual);
    }
}
