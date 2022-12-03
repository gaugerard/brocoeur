package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.UserWinLossByGame;
import brocoeur.example.analytics.repository.UserWinLossByGameRepository;
import brocoeur.example.broker.common.AnalyticServiceRequestTypes;
import brocoeur.example.broker.common.request.AnalyticServiceRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

        Flux<UserWinLossByGame> fluxFindByGameIdAndUserId = Flux.just(userWinLossByGame);
        Mono<UserWinLossByGame> monoSave = Mono.just(userWinLossByGame);
        Mockito.when(userWinLossByGameRepositoryMock.findByGameIdAndUserId(gameId, userId)).thenReturn(fluxFindByGameIdAndUserId);
        Mockito.when(userWinLossByGameRepositoryMock.save(userWinLossByGameUpdated)).thenReturn(monoSave);

        // When
        userWinLossByGameService.updateAnalyticAccordingToWinOrLoss(analyticServiceRequest);

        // Then
        Mockito.verify(userWinLossByGameRepositoryMock).save(userWinLossByGameUpdated);
        Mockito.verifyNoMoreInteractions(userWinLossByGameRepositoryMock);
        Mockito.verify(serviceRequestStatusServiceMock).updateServiceRequestStatusByJobIdAndUpdatePlayerMoney(linkedJobId, listOfIsWinner, amount);
        Mockito.verifyNoMoreInteractions(serviceRequestStatusServiceMock);
    }
}
