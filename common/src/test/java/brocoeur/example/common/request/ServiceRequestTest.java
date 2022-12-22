package brocoeur.example.common.request;

import brocoeur.example.common.GameStrategyTypes;
import brocoeur.example.common.ServiceRequestTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.springframework.test.util.AssertionErrors.assertTrue;

class ServiceRequestTest {

    @TempDir
    private File tempDir;

    @Test
    void shouldCreateJsonFromServiceRequest() throws IOException {
        // Given
        var objectMapper = new ObjectMapper();
        var playerRequest = new PlayerRequest("12345", GameStrategyTypes.ROULETTE_RISKY, null, 5, null);
        var serviceRequest = new ServiceRequest(ServiceRequestTypes.DIRECT, playerRequest, null);
        var serviceRequestFile = new File(tempDir, "serviceRequest.json");

        // When
        objectMapper.writeValue(serviceRequestFile, serviceRequest);

        // Then
        assertTrue("File should exist", Files.exists(serviceRequestFile.toPath()));
    }

    @Test
    void shouldCreateServiceRequestFromJson() throws IOException {
        // Given
        var objectMapper = new ObjectMapper();

        // When
        var actual = objectMapper.readValue(new File("src/test/resources/serviceRequestCoinTossRandom.json"), ServiceRequest.class);

        // Then
        var expectedPlayerRequest = new PlayerRequest("8", GameStrategyTypes.COIN_TOSS_RANDOM, null, 50, 345543);
        var expectedServiceRequest = new ServiceRequest(ServiceRequestTypes.DIRECT, expectedPlayerRequest, null);
        Assertions.assertEquals(actual, expectedServiceRequest);
    }
}
