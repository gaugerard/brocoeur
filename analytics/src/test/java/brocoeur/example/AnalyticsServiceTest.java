package nerima.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @InjectMocks
    private AnalyticsService analyticsService;

    public static Stream<Arguments> getInput() {
        return Stream.of(
                Arguments.of("Test name", "test", "test"),
                Arguments.of("Test name 2", "test2", "test2")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getInput")
    void shouldTestAnalyse(final String name, final String input, final String expectedResult) {
        // Given - When
        var actualContent = analyticsService.analyse(input);

        // Then
        Assertions.assertEquals(expectedResult, actualContent);
    }

}
