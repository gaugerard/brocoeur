package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.UserWinLossByGame;
import brocoeur.example.analytics.repository.UserWinLossByGameRepository;
import brocoeur.example.common.AnalyticServiceRequestTypes;
import brocoeur.example.common.request.AnalyticServiceRequest;
import brocoeur.example.common.request.PlayerResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

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
        var amount = 100;
        var linkedJobId = 789456;
        var playerResponse = new PlayerResponse(gameId, userId, 50, amount, linkedJobId);
        var analyticServiceRequest = new AnalyticServiceRequest(
                AnalyticServiceRequestTypes.MONEY_MANAGEMENT,
                playerResponse);

        var userWinLossByGame = new UserWinLossByGame(gameId, userId, "Roulette", "Baba", 0);
        var userWinLossByGameUpdated = new UserWinLossByGame(gameId, userId, "Roulette", "Baba", 50);

        var monoFindByGameIdAndUserId = Mono.just(userWinLossByGame);
        var monoSave = Mono.just(userWinLossByGame);
        when(userWinLossByGameRepositoryMock.findByGameIdAndUserId(gameId, userId)).thenReturn(monoFindByGameIdAndUserId);
        when(userWinLossByGameRepositoryMock.save(userWinLossByGameUpdated)).thenReturn(monoSave);

        // When
        userWinLossByGameService.manageAnalyticAccordingToWinOrLoss(analyticServiceRequest);

        // Then
        verify(userWinLossByGameRepositoryMock).save(userWinLossByGameUpdated);
        verifyNoMoreInteractions(userWinLossByGameRepositoryMock);
        verify(serviceRequestStatusServiceMock).updateServiceRequestStatusByJobIdAndUpdatePlayerMoney(linkedJobId, amount);
        verifyNoMoreInteractions(serviceRequestStatusServiceMock);
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenUserWinLossByGameIsNull() {
        // Given
        var gameId = 123;
        var userId = 8;
        var amount = 100;
        var linkedJobId = 789456;
        var playerResponse = new PlayerResponse(gameId, userId, 50, amount, linkedJobId);
        var analyticServiceRequest = new AnalyticServiceRequest(
                AnalyticServiceRequestTypes.MONEY_MANAGEMENT,
                playerResponse);

        when(userWinLossByGameRepositoryMock.findByGameIdAndUserId(gameId, userId)).thenReturn(Mono.empty());

        // When
        var exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            userWinLossByGameService.manageAnalyticAccordingToWinOrLoss(analyticServiceRequest);
        });

        Assertions.assertEquals("UserWinLossByGame with gameId : 123 and userId : 8 do not exists.", exception.getMessage());

        // Then
        verifyNoMoreInteractions(userWinLossByGameRepositoryMock);
        verifyNoInteractions(serviceRequestStatusServiceMock);
    }
}
