package brocoeur.example.analytics.service.scheduler;

import brocoeur.example.analytics.model.ServiceRequestStatus;
import brocoeur.example.analytics.service.ServiceRequestStatusService;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.ServiceRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static brocoeur.example.analytics.service.ServiceRequestStatusStatus.IN_PROGRESS;
import static brocoeur.example.analytics.service.ServiceRequestStatusStatus.TODO;
import static brocoeur.example.common.GameStrategyTypes.*;
import static brocoeur.example.common.ServiceRequestTypes.MULTIPLAYER;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokerScheduledTaskTest {

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
        var findAllByStrategyAndStatus = List.of(
                serviceRequestStatus1,
                serviceRequestStatus2,
                serviceRequestStatus3,
                serviceRequestStatus4
        );
        when(serviceRequestStatusServiceMock.findAllServiceRequestStatusByStrategyAndStatus(POKER_RANDOM.toString(), TODO)).thenReturn(findAllByStrategyAndStatus);

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
        var findAllByStrategyAndStatus = List.of(
                serviceRequestStatus1,
                serviceRequestStatus2
        );
        when(serviceRequestStatusServiceMock.findAllServiceRequestStatusByStrategyAndStatus(POKER_RANDOM.toString(), TODO)).thenReturn(findAllByStrategyAndStatus);

        // When
        pokerScheduledTask.executeScheduledTask();

        // Then
        Mockito.verify(serviceRequestStatusServiceMock).findAllServiceRequestStatusByStrategyAndStatus(POKER_RANDOM.toString(), TODO);
        Mockito.verifyNoMoreInteractions(serviceRequestStatusServiceMock);
    }

    @Test
    void shouldTestRejectBlockedRequestsWithOneBlockedRequest(){

        // Given
        ServiceRequestStatus blockedRequest = new ServiceRequestStatus(
                123,
                IN_PROGRESS.label,
                200,
                8,
                ROULETTE_RISKY.name(),
                (int)new Date().getTime() - 10000,
                0
        );

        when(serviceRequestStatusServiceMock.findAllServiceRequestStatusByStatus(IN_PROGRESS)).thenReturn(List.of(blockedRequest));

        // When
        pokerScheduledTask.rejectBlockedRequests();

        // Then
        Mockito.verify(serviceRequestStatusServiceMock).rejectServiceRequestStatus(blockedRequest);
    }


    @Test
    void shouldTestRejectBlockedRequestsWithoutBlockingRequest(){

        // Given
        ServiceRequestStatus blockedRequest = new ServiceRequestStatus(
                123,
                IN_PROGRESS.label,
                200,
                8,
                ROULETTE_RISKY.name(),
                (int)new Date().getTime(),
                0
        );

        ServiceRequestStatus secondRequest = new ServiceRequestStatus(
                124,
                IN_PROGRESS.label,
                100,
                8,
                ROULETTE_SAFE.name(),
                (int)new Date().getTime() - 500,
                0
        );

        when(serviceRequestStatusServiceMock.findAllServiceRequestStatusByStatus(IN_PROGRESS)).thenReturn(List.of(blockedRequest, secondRequest));

        // When
        pokerScheduledTask.rejectBlockedRequests();

        // Then
        Mockito.verify(serviceRequestStatusServiceMock).findAllServiceRequestStatusByStatus(IN_PROGRESS);
        Mockito.verifyNoMoreInteractions(serviceRequestStatusServiceMock);
    }

}
