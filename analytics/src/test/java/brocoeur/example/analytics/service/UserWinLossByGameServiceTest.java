package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.UserWinLossByGame;
import brocoeur.example.analytics.repository.UserWinLossByGameRepository;
import brocoeur.example.common.AnalyticServiceRequestTypes;
import brocoeur.example.common.request.AnalyticServiceRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserWinLossByGameServiceTest {

    @Mock
    private UserWinLossByGameRepository userWinLossByGameRepositoryMock;
    @Mock
    private ServiceRequestStatusService serviceRequestStatusServiceMock;

    @InjectMocks
    private UserWinLossByGameService userWinLossByGameService;

    @Test
    void shouldUpdateAnalyticAccordingToWinOrLoss() {
        // Given
        var gameId = 123;
        var userId = 8;
        var linkedJobId = 789456;
        var listOfIsWinner = List.of(true, false, false);
        var amount = 100;
        var analyticServiceRequest = new AnalyticServiceRequest(
                AnalyticServiceRequestTypes.MONEY_MANAGEMENT,
                123,
                8,
                listOfIsWinner,
                amount,
                linkedJobId
        );
        var userWinLossByGame = new UserWinLossByGame(gameId, userId, "Roulette", "Baba", 0, 0);
        var userWinLossByGameUpdated = new UserWinLossByGame(gameId, userId, "Roulette", "Baba", 1, 2);

        var monoFindByGameIdAndUserId = Mono.just(userWinLossByGame);
        var monoSave = Mono.just(userWinLossByGame);
        when(userWinLossByGameRepositoryMock.findByGameIdAndUserId(gameId, userId)).thenReturn(monoFindByGameIdAndUserId);
        when(userWinLossByGameRepositoryMock.save(userWinLossByGameUpdated)).thenReturn(monoSave);

        // When
        userWinLossByGameService.updateAnalyticAccordingToWinOrLoss(analyticServiceRequest);

        // Then
        verify(userWinLossByGameRepositoryMock).save(userWinLossByGameUpdated);
        verifyNoMoreInteractions(userWinLossByGameRepositoryMock);
        verify(serviceRequestStatusServiceMock).updateServiceRequestStatusByJobIdAndUpdatePlayerMoney(linkedJobId, listOfIsWinner, amount);
        verifyNoMoreInteractions(serviceRequestStatusServiceMock);
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenUserWinLossByGameIsNull() {
        // Given
        var gameId = 123;
        var userId = 8;
        var linkedJobId = 789456;
        var listOfIsWinner = List.of(true, false, false);
        var amount = 100;
        var analyticServiceRequest = new AnalyticServiceRequest(
                AnalyticServiceRequestTypes.MONEY_MANAGEMENT,
                123,
                8,
                listOfIsWinner,
                amount,
                linkedJobId
        );

        when(userWinLossByGameRepositoryMock.findByGameIdAndUserId(gameId, userId)).thenReturn(Mono.empty());

        // When
        var exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            userWinLossByGameService.updateAnalyticAccordingToWinOrLoss(analyticServiceRequest);
        });

        Assertions.assertEquals("UserWinLossByGame with gameId : 123 and userId : 8 do not exists.", exception.getMessage());

        // Then
        verifyNoMoreInteractions(userWinLossByGameRepositoryMock);
        verifyNoInteractions(serviceRequestStatusServiceMock);
    }
}
