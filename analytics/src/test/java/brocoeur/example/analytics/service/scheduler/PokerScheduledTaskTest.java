package brocoeur.example.analytics.service.scheduler;

import brocoeur.example.analytics.model.ServiceRequestStatus;
import brocoeur.example.analytics.repository.ServiceRequestStatusRepository;
import brocoeur.example.analytics.service.ServiceRequestStatusService;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.ServiceRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.List;

import static brocoeur.example.common.GameStrategyTypes.POKER_RANDOM;
import static brocoeur.example.common.ServiceRequestTypes.MULTIPLAYER;

@ExtendWith(MockitoExtension.class)
class PokerScheduledTaskTest {
    @Mock
    private ServiceRequestStatusRepository serviceRequestStatusRepositoryMock;
    @Mock
    private ServiceRequestStatusService serviceRequestStatusServiceMock;
    @InjectMocks
    private PokerScheduledTask pokerScheduledTask;

    @Test
    void shouldTestExecuteScheduledTaskWhenAtLeast3PokerRequests() {
        // Given
        var serviceRequestStatus1 = new ServiceRequestStatus(1, "TODO", 10, 8, "POKER_RANDOM", 10000, 0);
        var serviceRequestStatus2 = new ServiceRequestStatus(2, "TODO", 15, 9, "POKER_RANDOM", 10001, 0);
        var serviceRequestStatus3 = new ServiceRequestStatus(3, "TODO", 20, 10, "POKER_RANDOM", 10002, 0);
        var serviceRequestStatus4 = new ServiceRequestStatus(4, "TODO", 25, 11, "POKER_RANDOM", 10002, 0);
        var fluxFindAllByStrategyAndStatus = Flux.just(
                serviceRequestStatus1,
                serviceRequestStatus2,
                serviceRequestStatus3,
                serviceRequestStatus4
        );
        Mockito.when(serviceRequestStatusRepositoryMock.findAllByStrategyAndStatus(POKER_RANDOM.toString(), "TODO")).thenReturn(fluxFindAllByStrategyAndStatus);

        // When
        pokerScheduledTask.executeScheduledTask();

        // Then
        var playerRequest1 = new PlayerRequest("8", POKER_RANDOM, null, 10, 1);
        var playerRequest2 = new PlayerRequest("9", POKER_RANDOM, null, 15, 2);
        var playerRequest3 = new PlayerRequest("10", POKER_RANDOM, null, 20, 3);
        var playerRequestList = List.of(playerRequest1, playerRequest2, playerRequest3);

        var serviceRequest = new ServiceRequest(MULTIPLAYER, playerRequestList, null);
        Mockito.verify(serviceRequestStatusServiceMock).sendMultiplayerServiceRequest(serviceRequest);
    }

    @Test
    void shouldTestExecuteScheduledTaskWhenNotEnoughPokerRequests() {
        // Given
        var serviceRequestStatus1 = new ServiceRequestStatus(1, "TODO", 10, 8, "POKER_RANDOM", 10000, 0);
        var serviceRequestStatus2 = new ServiceRequestStatus(2, "TODO", 15, 9, "POKER_RANDOM", 10001, 0);
        var fluxFindAllByStrategyAndStatus = Flux.just(
                serviceRequestStatus1,
                serviceRequestStatus2
        );
        Mockito.when(serviceRequestStatusRepositoryMock.findAllByStrategyAndStatus(POKER_RANDOM.toString(), "TODO")).thenReturn(fluxFindAllByStrategyAndStatus);

        // When
        pokerScheduledTask.executeScheduledTask();

        // Then
        Mockito.verifyNoInteractions(serviceRequestStatusServiceMock);
    }
}
