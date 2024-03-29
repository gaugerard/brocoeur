package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.ServiceRequestStatus;
import brocoeur.example.analytics.model.UserMoney;
import brocoeur.example.analytics.repository.ServiceRequestStatusRepository;
import brocoeur.example.analytics.repository.UserMoneyRepository;
import brocoeur.example.analytics.service.utils.RandomService;
import brocoeur.example.common.ServiceRequestTypes;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static brocoeur.example.analytics.service.ServiceRequestStatusStatus.*;
import static brocoeur.example.common.ServiceRequestTypes.MULTIPLAYER;

@Service
public class ServiceRequestStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestStatusService.class);

    @Autowired
    private ServiceRequestStatusRepository serviceRequestStatusRepository;
    @Autowired
    private UserMoneyRepository userMoneyRepository;
    @Autowired
    private RandomService randomService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void deleteAllServiceRequestStatus() {
        serviceRequestStatusRepository.deleteAll().subscribe();
    }

    /**
     * Sets poker serviceRequest to IN_PROGRESS and sends the poker ServiceRequest.
     */
    public void sendMultiplayerServiceRequest(final ServiceRequest serviceRequest) {
        // Update status of the 3 poker request.
        for (PlayerRequest playerRequest : serviceRequest.getPlayerRequestList()) {
            final int jobId = playerRequest.getLinkedJobId();
            final ServiceRequestStatus serviceRequestStatus = serviceRequestStatusRepository.findById(jobId).block();
            serviceRequestStatus.setStatus(IN_PROGRESS.label);
            serviceRequestStatusRepository.save(serviceRequestStatus).subscribe(updated -> LOGGER.info("Updated : {}", updated));
        }

        // Sends the poker request to 'Game' module.
        rabbitTemplate.convertAndSend("myexchange1", "MyQ1", serviceRequest);
    }

    /**
     * <p>This method checks if user has enough money to play the requested 'ServiceRequest'.</p>
     * <ul>
     *     <li>If YES: It blocks the requested amount of money from the user, add a line in 'serviceRequestStatus' Cassandra table (with status <b>IN_PROGRESS</b>) and sends the 'ServiceRequest'.</li>
     *     <li>If NO: It only add a line in 'serviceRequestStatus' Cassandra table (with status <b>REJECTED</b>).</li>
     * </ul>
     */
    public void addServiceRequestStatus(final ServiceRequest serviceRequest) {
        final int jobId = randomService.getRandomJobId();

        if (serviceRequest.getPlayerRequestList().size() != 1) {
            throw new IllegalStateException("ServiceRequest.PlayerRequestList must be of size 1.");
        }

        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final int userId = Integer.parseInt(playerRequest.getUserId());

        final UserMoney userMoney = userMoneyRepository.findById(userId).block();
        if (userMoney == null) {
            throw new IllegalStateException("UserMoney for userId " + userId + " do not exists.");
        }

        final int amountOfMoneyToBlock = playerRequest.getAmountToGamble();
        final ServiceRequestTypes serviceRequestTypes = serviceRequest.getServiceRequestTypes();
        final int totalAmountOfMoneyAvailable = userMoney.getMoney();
        final long currentTimeInMilliseconds = randomService.getCurrentTimeInMilliseconds();
        final String gameStrategyTypes = playerRequest.getGameStrategyTypes().toString();

        if (amountOfMoneyToBlock <= totalAmountOfMoneyAvailable) {

            // Update total amount of money available for the user.
            userMoney.setMoney(totalAmountOfMoneyAvailable - amountOfMoneyToBlock);
            userMoneyRepository.save(userMoney).subscribe(updated -> LOGGER.info("Updated : {}", updated));

            final String status = (MULTIPLAYER.equals(serviceRequestTypes)) ? TODO.label : IN_PROGRESS.label;

            // Insert new line in 'serviceRequestStatus' table.
            final ServiceRequestStatus serviceRequestStatus = new ServiceRequestStatus(
                    jobId,
                    status,
                    amountOfMoneyToBlock,
                    userId,
                    gameStrategyTypes,
                    currentTimeInMilliseconds,
                    0
            );

            serviceRequestStatusRepository.save(serviceRequestStatus).subscribe(updated -> LOGGER.info("Saved : {}", updated));

            if (MULTIPLAYER.equals(serviceRequestTypes)) {
                LOGGER.info("Multiplayer request is saved and will be played later or cancelled after 5 minutes.");
            } else {
                // Send 'ServiceRequest' to Game module.
                playerRequest.setLinkedJobId(jobId);
                rabbitTemplate.convertAndSend("myexchange1", "MyQ1", serviceRequest);
            }
        } else {
            final ServiceRequestStatus serviceRequestStatus = new ServiceRequestStatus(
                    jobId,
                    REJECTED.label,
                    amountOfMoneyToBlock,
                    userId,
                    gameStrategyTypes,
                    currentTimeInMilliseconds,
                    currentTimeInMilliseconds
            );
            serviceRequestStatusRepository.save(serviceRequestStatus).subscribe(updated -> LOGGER.info("Saved : {}", updated));
        }

    }

    /**
     * <p>This method update the status of the selected 'serviceRequestStatus' line in Cassandra and give money to the user if he/she won the game.</p>
     * <p>For now, when a user wins a game round, the amount gambled is <b>DOUBLED</b> for the said round.</p>
     */
    public void updateServiceRequestStatusByJobIdAndUpdatePlayerMoney(final int jobId, final int totalAmountWon) {
        final ServiceRequestStatus serviceRequestStatus = serviceRequestStatusRepository.findById(jobId).block();

        if (serviceRequestStatus == null) {
            throw new IllegalStateException("ServiceRequestStatus for jobId " + jobId + " do not exists.");
        }

        final int userId = serviceRequestStatus.getUserId();

        serviceRequestStatus.setAckTimeMilliSecond(randomService.getCurrentTimeInMilliseconds());

        final int amountBlocked = serviceRequestStatus.getAmountBlocked();

        final UserMoney userMoney = userMoneyRepository.findById(userId).block();

        if (userMoney == null) {
            throw new IllegalStateException("UserMoney for userId " + userId + " do not exists.");
        }

        final int totalAmountOfMoneyAvailable = userMoney.getMoney();

        if (totalAmountWon > amountBlocked) {
            serviceRequestStatus.setStatus(DONE_WIN.label);
        } else {
            serviceRequestStatus.setStatus(DONE_LOSS.label);
        }

        userMoney.setMoney(totalAmountOfMoneyAvailable + totalAmountWon);
        userMoneyRepository.save(userMoney).subscribe(updated -> LOGGER.info("Updated : {}", updated));
        serviceRequestStatusRepository.save(serviceRequestStatus).subscribe(updated -> LOGGER.info("Updated : {}", updated));
    }

    public void cancelServiceRequestStatus(ServiceRequestStatus serviceRequestStatus) {
        serviceRequestStatus.setStatus(CANCELLED.label);
        serviceRequestStatusRepository.save(serviceRequestStatus).subscribe(updated -> LOGGER.info("Updated : {}", updated));
    }

    public List<ServiceRequestStatus> findAllServiceRequestStatusByStatus(ServiceRequestStatusStatus serviceRequestStatusStatus) {
        return serviceRequestStatusRepository.findAllByStatus(serviceRequestStatusStatus.label).collectList().block();
    }

    public List<ServiceRequestStatus> findAllServiceRequestStatusByStrategyAndStatus(String strategy, ServiceRequestStatusStatus serviceRequestStatusStatus) {
        return serviceRequestStatusRepository.findAllByStrategyAndStatus(strategy, serviceRequestStatusStatus.label).collectList().block();
    }
}
