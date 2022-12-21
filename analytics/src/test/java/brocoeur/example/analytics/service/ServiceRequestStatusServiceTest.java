package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.ServiceRequestStatus;
import brocoeur.example.analytics.model.UserMoney;
import brocoeur.example.analytics.repository.ServiceRequestStatusRepository;
import brocoeur.example.analytics.repository.UserMoneyRepository;
import brocoeur.example.analytics.service.utils.RandomService;
import brocoeur.example.common.ServiceRequestTypes;
import brocoeur.example.common.request.ServiceRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

import static brocoeur.example.common.GameStrategyTypes.COIN_TOSS_RANDOM;
import static brocoeur.example.common.GameStrategyTypes.POKER_RANDOM;
import static brocoeur.example.common.OfflineGameStrategyTypes.OFFLINE_COIN_TOSS_RANDOM;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceRequestStatusServiceTest {

    @Mock
    private ServiceRequestStatusRepository serviceRequestStatusRepositoryMock;
    @Mock
    private UserMoneyRepository userMoneyRepositoryMock;
    @Mock
    private RandomService randomServiceMock;
    @Mock
    private RabbitTemplate rabbitTemplateMock;

    @InjectMocks
    private ServiceRequestStatusService serviceRequestStatusService;

    @Nested
    @DisplayName("Tests for DIRECT ServiceRequestStatusService")
    class DirectServiceRequestStatusService {

        @Test
        void shouldAddServiceRequestStatusWhenEnoughMoney() {
            // Given
            var jobId = 156478;
            var currentTimeInSeconds = 1400000;
            var directServiceRequest = new ServiceRequest(
                    "8",
                    COIN_TOSS_RANDOM,
                    50,
                    0
            );
            var userMoney = new UserMoney(8, 1000);
            var userMoneyUpdated = new UserMoney(8, 950);
            var serviceRequestStatus = new ServiceRequestStatus(
                    jobId,
                    "IN_PROGRESS",
                    50,
                    8,
                    COIN_TOSS_RANDOM.toString(),
                    currentTimeInSeconds,
                    0
            );
            var monoFindById = Mono.just(userMoney);
            var monoSaveUserMoney = Mono.just(userMoneyUpdated);
            var monoSaveServiceRequestStatus = Mono.just(serviceRequestStatus);

            when(randomServiceMock.getRandomJobId()).thenReturn(jobId);
            when(userMoneyRepositoryMock.findById(8)).thenReturn(monoFindById);
            when(userMoneyRepositoryMock.save(userMoneyUpdated)).thenReturn(monoSaveUserMoney);
            when(randomServiceMock.getCurrentTimeInSeconds()).thenReturn(currentTimeInSeconds);
            when(serviceRequestStatusRepositoryMock.save(serviceRequestStatus)).thenReturn(monoSaveServiceRequestStatus);

            // When
            serviceRequestStatusService.addServiceRequestStatus(directServiceRequest);

            // Then
            var expectedDirectServiceRequest = new ServiceRequest(
                    "8",
                    COIN_TOSS_RANDOM,
                    50,
                    jobId
            );
            verify(userMoneyRepositoryMock).save(userMoneyUpdated);
            verifyNoMoreInteractions(userMoneyRepositoryMock);
            verify(serviceRequestStatusRepositoryMock).save(serviceRequestStatus);
            verifyNoMoreInteractions(serviceRequestStatusRepositoryMock);
            verify(rabbitTemplateMock).convertAndSend("myexchange1", "MyQ1", expectedDirectServiceRequest);
            verifyNoMoreInteractions(rabbitTemplateMock);
        }

        @Test
        void shouldAddServiceRequestStatusWhenNotEnoughMoney() {
            // Given
            var jobId = 156478;
            var currentTimeInSeconds = 1400000;
            var directServiceRequest = new ServiceRequest(
                    "8",
                    COIN_TOSS_RANDOM,
                    800,
                    0
            );
            var userMoney = new UserMoney(8, 150);
            var serviceRequestStatus = new ServiceRequestStatus(
                    jobId,
                    "REJECTED",
                    800,
                    8,
                    COIN_TOSS_RANDOM.toString(),
                    currentTimeInSeconds,
                    currentTimeInSeconds
            );
            var monoFindById = Mono.just(userMoney);
            var monoSaveServiceRequestStatus = Mono.just(serviceRequestStatus);

            when(randomServiceMock.getRandomJobId()).thenReturn(jobId);
            when(userMoneyRepositoryMock.findById(8)).thenReturn(monoFindById);
            when(randomServiceMock.getCurrentTimeInSeconds()).thenReturn(currentTimeInSeconds);
            when(serviceRequestStatusRepositoryMock.save(serviceRequestStatus)).thenReturn(monoSaveServiceRequestStatus);

            // When
            serviceRequestStatusService.addServiceRequestStatus(directServiceRequest);

            // Then
            verifyNoMoreInteractions(userMoneyRepositoryMock);
            verify(serviceRequestStatusRepositoryMock).save(serviceRequestStatus);
            verifyNoMoreInteractions(serviceRequestStatusRepositoryMock);
            verifyNoInteractions(rabbitTemplateMock);
        }

        @Test
        void shouldThrowIllegalStateExceptionWhenAddServiceRequestStatusUserIdDoesNotExists() {
            // Given
            var jobId = 156478;
            var directServiceRequest = new ServiceRequest(
                    "10",
                    COIN_TOSS_RANDOM,
                    800,
                    0
            );

            when(randomServiceMock.getRandomJobId()).thenReturn(jobId);
            when(userMoneyRepositoryMock.findById(10)).thenReturn(Mono.empty());

            // When
            var exception = Assertions.assertThrows(IllegalStateException.class, () -> {
                serviceRequestStatusService.addServiceRequestStatus(directServiceRequest);
            });

            Assertions.assertEquals("UserMoney for userId 10 do not exists.", exception.getMessage());

            // Then
            verifyNoMoreInteractions(randomServiceMock);
            verifyNoMoreInteractions(userMoneyRepositoryMock);
            verifyNoInteractions(serviceRequestStatusRepositoryMock);
            verifyNoInteractions(rabbitTemplateMock);
        }

        @Test
        void shouldUpdateServiceRequestStatusByJobIdAndUpdatePlayerMoney() {
            // Given
            var jobId = 135445;
            var listOfIsWinner = List.of(true);
            var amountGambled = 80;
            var userId = 8;

            var serviceRequestStatus = new ServiceRequestStatus(jobId, "IN_PROGESS", 80, userId, COIN_TOSS_RANDOM.toString(), 12340, 0);
            var monoFindById = Mono.just(serviceRequestStatus);
            when(serviceRequestStatusRepositoryMock.findById(jobId)).thenReturn(monoFindById);
            when(randomServiceMock.getCurrentTimeInSeconds()).thenReturn(13245);

            var userMoney = new UserMoney(userId, 100);
            var monoFindByIdUser = Mono.just(userMoney);
            when(userMoneyRepositoryMock.findById(userId)).thenReturn(monoFindByIdUser);

            var userMoneySave = new UserMoney(userId, 260);
            var monoSaveUpdated = Mono.just(userMoney);
            when(userMoneyRepositoryMock.save(userMoneySave)).thenReturn(monoSaveUpdated);

            var serviceRequestStatusDoneWin = new ServiceRequestStatus(jobId, "DONE_WIN", 80, userId, COIN_TOSS_RANDOM.toString(), 12340, 13245);
            var monoSaveDoneWin = Mono.just(serviceRequestStatus);
            when(serviceRequestStatusRepositoryMock.save(serviceRequestStatusDoneWin)).thenReturn(monoSaveDoneWin);

            // When
            serviceRequestStatusService.updateServiceRequestStatusByJobIdAndUpdatePlayerMoney(jobId, listOfIsWinner, amountGambled);

            // Then
            verify(userMoneyRepositoryMock).save(userMoneySave);
            verifyNoMoreInteractions(randomServiceMock);
            verifyNoMoreInteractions(userMoneyRepositoryMock);
            verify(serviceRequestStatusRepositoryMock).save(serviceRequestStatusDoneWin);
            verifyNoMoreInteractions(serviceRequestStatusRepositoryMock);
            Mockito.verifyNoInteractions(rabbitTemplateMock);
        }

        @Test
        void shouldThrowIllegalStateExceptionWhenUpdateServiceRequestStatusIsNull() {
            // Given
            var jobId = 333333;
            var listOfIsWinner = List.of(true);
            var amountGambled = 80;

            when(serviceRequestStatusRepositoryMock.findById(jobId)).thenReturn(Mono.empty());

            // When
            var exception = Assertions.assertThrows(IllegalStateException.class, () -> {
                serviceRequestStatusService.updateServiceRequestStatusByJobIdAndUpdatePlayerMoney(jobId, listOfIsWinner, amountGambled);
            });

            Assertions.assertEquals("ServiceRequestStatus for jobId 333333 do not exists.", exception.getMessage());

            // Then
            verifyNoMoreInteractions(serviceRequestStatusRepositoryMock);
            verifyNoInteractions(randomServiceMock);
            verifyNoInteractions(userMoneyRepositoryMock);
            Mockito.verifyNoInteractions(rabbitTemplateMock);
        }

        @Test
        void shouldThrowIllegalStateExceptionWhenUserMoneyIsNull() {
            // Given
            var jobId = 135445;
            var listOfIsWinner = List.of(true);
            var amountGambled = 80;
            var userId = 12;

            var serviceRequestStatus = new ServiceRequestStatus(jobId, "IN_PROGESS", 80, userId, COIN_TOSS_RANDOM.toString(), 12340, 0);
            var monoFindById = Mono.just(serviceRequestStatus);
            when(serviceRequestStatusRepositoryMock.findById(jobId)).thenReturn(monoFindById);
            when(randomServiceMock.getCurrentTimeInSeconds()).thenReturn(13245);

            when(userMoneyRepositoryMock.findById(userId)).thenReturn(Mono.empty());

            // When
            var exception = Assertions.assertThrows(IllegalStateException.class, () -> {
                serviceRequestStatusService.updateServiceRequestStatusByJobIdAndUpdatePlayerMoney(jobId, listOfIsWinner, amountGambled);
            });

            Assertions.assertEquals("UserMoney for userId 12 do not exists.", exception.getMessage());

            // Then
            verifyNoMoreInteractions(randomServiceMock);
            verifyNoMoreInteractions(userMoneyRepositoryMock);
            verifyNoMoreInteractions(serviceRequestStatusRepositoryMock);
            verifyNoInteractions(rabbitTemplateMock);
        }
    }

    @Nested
    @DisplayName("Tests for OFFLINE ServiceRequestStatusService")
    class OfflineServiceRequestStatusService {

        @Test
        void shouldAddServiceRequestStatusWhenEnoughMoney() {
            // Given
            var jobId = 156478;
            var currentTimeInSeconds = 1400000;
            var offlineServiceRequest = new ServiceRequest(
                    "8",
                    OFFLINE_COIN_TOSS_RANDOM,
                    3,
                    50,
                    156478
            );
            var userMoney = new UserMoney(8, 1000);
            var userMoneyUpdated = new UserMoney(8, 850);
            var serviceRequestStatus = new ServiceRequestStatus(
                    jobId,
                    "IN_PROGRESS",
                    150,
                    8,
                    OFFLINE_COIN_TOSS_RANDOM.toString(),
                    currentTimeInSeconds,
                    0
            );
            var monoFindById = Mono.just(userMoney);
            var monoSaveUserMoney = Mono.just(userMoneyUpdated);
            var monoSaveServiceRequestStatus = Mono.just(serviceRequestStatus);

            when(randomServiceMock.getRandomJobId()).thenReturn(jobId);
            when(userMoneyRepositoryMock.findById(8)).thenReturn(monoFindById);
            when(userMoneyRepositoryMock.save(userMoneyUpdated)).thenReturn(monoSaveUserMoney);
            when(randomServiceMock.getCurrentTimeInSeconds()).thenReturn(currentTimeInSeconds);
            when(serviceRequestStatusRepositoryMock.save(serviceRequestStatus)).thenReturn(monoSaveServiceRequestStatus);


            // When
            serviceRequestStatusService.addServiceRequestStatus(offlineServiceRequest);

            // Then
            var expectedOfflineServiceRequest = new ServiceRequest(
                    "8",
                    OFFLINE_COIN_TOSS_RANDOM,
                    3,
                    50,
                    jobId
            );
            verify(userMoneyRepositoryMock).save(userMoneyUpdated);
            verifyNoMoreInteractions(userMoneyRepositoryMock);
            verify(serviceRequestStatusRepositoryMock).save(serviceRequestStatus);
            verifyNoMoreInteractions(serviceRequestStatusRepositoryMock);
            verify(rabbitTemplateMock).convertAndSend("myexchange1", "MyQ1", expectedOfflineServiceRequest);
            verifyNoMoreInteractions(rabbitTemplateMock);
        }
    }

    @Nested
    @DisplayName("Tests for MULTIPLAYER ServiceRequestStatusService")
    class MultiplayerServiceRequestStatusService {

        @Test
        void shouldAddServiceRequestStatusWhenEnoughMoney() {
            // Given
            var jobId = 156478;
            var currentTimeInSeconds = 1400000;
            var directServiceRequest = new ServiceRequest(
                    ServiceRequestTypes.MULTIPLAYER,
                    "8",
                    POKER_RANDOM,
                    null,
                    null,
                    50,
                    null
            );
            var userMoney = new UserMoney(8, 1000);
            var userMoneyUpdated = new UserMoney(8, 950);
            var serviceRequestStatus = new ServiceRequestStatus(
                    jobId,
                    "TODO",
                    50,
                    8,
                    POKER_RANDOM.toString(),
                    currentTimeInSeconds,
                    0
            );
            var monoFindById = Mono.just(userMoney);
            var monoSaveUserMoney = Mono.just(userMoneyUpdated);
            var monoSaveServiceRequestStatus = Mono.just(serviceRequestStatus);

            when(randomServiceMock.getRandomJobId()).thenReturn(jobId);
            when(userMoneyRepositoryMock.findById(8)).thenReturn(monoFindById);
            when(userMoneyRepositoryMock.save(userMoneyUpdated)).thenReturn(monoSaveUserMoney);
            when(randomServiceMock.getCurrentTimeInSeconds()).thenReturn(currentTimeInSeconds);
            when(serviceRequestStatusRepositoryMock.save(serviceRequestStatus)).thenReturn(monoSaveServiceRequestStatus);

            // When
            serviceRequestStatusService.addServiceRequestStatus(directServiceRequest);

            // Then
            verify(userMoneyRepositoryMock).save(userMoneyUpdated);
            verifyNoMoreInteractions(userMoneyRepositoryMock);
            verify(serviceRequestStatusRepositoryMock).save(serviceRequestStatus);
            verifyNoMoreInteractions(serviceRequestStatusRepositoryMock);
            verifyNoInteractions(rabbitTemplateMock);
        }
    }
}
