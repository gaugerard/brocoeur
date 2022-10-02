package broker.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestPlzTest {

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void shouldTestAnalyse() {
        // Given - When
        analyticsService.analyse();
        // Then
    }

}
