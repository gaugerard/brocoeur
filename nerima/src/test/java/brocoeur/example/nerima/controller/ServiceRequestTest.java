package brocoeur.example.nerima.controller;

import brocoeur.example.nerima.controller.ServiceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static brocoeur.example.nerima.service.GameStrategyTypes.COIN_TOSS_RANDOM;
import static brocoeur.example.nerima.service.GameStrategyTypes.ROULETTE_RISKY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.springframework.test.util.AssertionErrors.assertTrue;

class ServiceRequestTest {

    @TempDir
    private File tempDir;

    @Test
    void shouldCreateJsonFromServiceRequest() throws IOException {
        // Given
        var objectMapper = new ObjectMapper();
        var serviceRequest = new ServiceRequest("12345", ROULETTE_RISKY);
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
        var expected = new ServiceRequest("67890", COIN_TOSS_RANDOM);
        assertThat(actual, samePropertyValuesAs(expected));
    }
}
