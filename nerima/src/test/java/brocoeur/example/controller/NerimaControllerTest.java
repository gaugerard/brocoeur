package brocoeur.example.controller;

import brocoeur.example.controller.NerimaController;
import brocoeur.example.controller.ServiceRequest;
import brocoeur.example.service.GameStrategyTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static brocoeur.example.service.GameStrategyTypes.*;

@ExtendWith(MockitoExtension.class)
class NerimaControllerTest {

    @InjectMocks
    private NerimaController nerimaController;

    public static Stream<Arguments> getInputForRoulette() {
        return Stream.of(
                Arguments.of("Test - Roulette Risky", ROULETTE_RISKY),
                Arguments.of("Test - Roulette Safe", ROULETTE_SAFE)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getInputForRoulette")
    void shouldTestRouletteRiskyStrategy(final String testName, final GameStrategyTypes rouletteStrategyType) {
        // Given
        var userId = "12345";
        var serviceRequest = new ServiceRequest(userId, rouletteStrategyType);

        // When
        var actual = nerimaController.postPlay(serviceRequest);

        // Then
        Assertions.assertEquals(HttpStatus.OK, actual);
    }

    @Test
    void shouldTestCoinTossStrategy() {
        // Given
        var userId = "67890";
        var serviceRequest = new ServiceRequest(userId, COIN_TOSS_RANDOM);

        // When
        var actual = nerimaController.postPlay(serviceRequest);

        // Then
        var expectedStart = "67890 used strategy: RandomCoinToss and will play: ";
        Assertions.assertEquals(HttpStatus.OK,actual);
    }
}
