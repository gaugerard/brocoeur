package brocoeur.example.analytics.service.scheduler;

import brocoeur.example.analytics.model.ServiceRequestStatus;
import brocoeur.example.analytics.service.ServiceRequestStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Date;

import static brocoeur.example.analytics.service.ServiceRequestStatusStatus.IN_PROGRESS;
import static brocoeur.example.common.GameStrategyTypes.ROULETTE_RISKY;
import static brocoeur.example.common.GameStrategyTypes.ROULETTE_SAFE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokerScheduledTaskTest {

    @Mock
    ServiceRequestStatusService serviceRequestStatusServiceMock;

    @InjectMocks
    PokerScheduledTask pokerScheduledTask;

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

        when(serviceRequestStatusServiceMock.findAllServiceRequestStatusByStatus(IN_PROGRESS)).thenReturn(Arrays.asList(blockedRequest));

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

        when(serviceRequestStatusServiceMock.findAllServiceRequestStatusByStatus(IN_PROGRESS)).thenReturn(Arrays.asList(blockedRequest,secondRequest));

        // When
        pokerScheduledTask.rejectBlockedRequests();

        // Then
        Mockito.verify(serviceRequestStatusServiceMock).findAllServiceRequestStatusByStatus(IN_PROGRESS);
        Mockito.verifyNoMoreInteractions(serviceRequestStatusServiceMock);
    }

}

