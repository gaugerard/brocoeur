package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.ServiceRequestStatus;
import brocoeur.example.analytics.model.UserMoney;
import brocoeur.example.analytics.repository.ServiceRequestStatusRepository;
import brocoeur.example.analytics.repository.UserMoneyRepository;
import brocoeur.example.analytics.service.utils.RandomService;
import brocoeur.example.common.ServiceRequestTypes;
import brocoeur.example.common.request.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRequestStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestStatusService.class);

    private static final String IN_PROGRESS = "IN_PROGRESS";
    private static final String DONE_WIN = "DONE_WIN";
    private static final String DONE_LOSS = "DONE_LOSS";
    private static final String REJECTED = "REJECTED"; // When not enough money.
    private static final int WIN_MULTIPLIER = 2;

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
     * <p>We only manage fixed amount gambles. This means that each game round, the same amount of money will be gambled.</p>
     * <p>In the case of 'Offline' game, we need to multiply the amount of money to be blocked by the number of games the user wants to take part in.</p>
     *
     * @param serviceRequestType
     * @param timeToLive
     * @param amountToGamble
     * @return
     */
    private int getAmountOfMoneyToBlock(final ServiceRequestTypes serviceRequestType, final Integer timeToLive, final int amountToGamble) {
        return switch (serviceRequestType) {
            case DIRECT -> amountToGamble;
            case OFFLINE -> amountToGamble * timeToLive;
        };
    }

    private String getGameStrategyTypes(final ServiceRequest serviceRequest) {
        return switch (serviceRequest.getServiceRequestTypes()) {
            case DIRECT -> serviceRequest.getGameStrategyTypes().toString();
            case OFFLINE -> serviceRequest.getOfflineGameStrategyTypes().toString();
        };
    }

    /**
     * <p>This method checks if user has enough money to play the requested 'ServiceRequest'.</p>
     * <ul>
     *     <li>If YES: It blocks the requested amount of money from the user, add a line in 'serviceRequestStatus' Cassandra table (with status <b>IN_PROGRESS</b>) and sends the 'ServiceRequest'.</li>
     *     <li>If NO: It only add a line in 'serviceRequestStatus' Cassandra table (with status <b>REJECTED</b>).</li>
     * </ul>
     *
     * @param serviceRequest
     */
    public void addServiceRequestStatus(final ServiceRequest serviceRequest) {
        final int jobId = randomService.getRandomJobId();
        final int userId = Integer.parseInt(serviceRequest.getUserId());

        final UserMoney userMoney = userMoneyRepository.findById(userId).block();
        if (userMoney == null) {
            throw new IllegalStateException("UserMoney for userId " + userId + " do not exists.");
        }

        final int amountToGamble = serviceRequest.getAmountToGamble();
        final int amountOfMoneyToBlock = getAmountOfMoneyToBlock(serviceRequest.getServiceRequestTypes(), serviceRequest.getTimeToLive(), amountToGamble);
        final int totalAmountOfMoneyAvailable = userMoney.getMoney();
        final int currentTimeInMilliseconds = randomService.getCurrentTimeInSeconds();
        final String gameStrategyTypes = getGameStrategyTypes(serviceRequest);

        if (amountOfMoneyToBlock <= totalAmountOfMoneyAvailable) {

            // Update total amount of money available for the user.
            userMoney.setMoney(totalAmountOfMoneyAvailable - amountOfMoneyToBlock);
            userMoneyRepository.save(userMoney).subscribe(updated -> LOGGER.info("Updated : {}", updated));

            // Insert new line in 'serviceRequestStatus' table.
            final ServiceRequestStatus serviceRequestStatus = new ServiceRequestStatus(
                    jobId,
                    IN_PROGRESS,
                    amountOfMoneyToBlock,
                    userId,
                    gameStrategyTypes,
                    currentTimeInMilliseconds,
                    0
            );

            serviceRequestStatusRepository.save(serviceRequestStatus).subscribe(updated -> LOGGER.info("Saved : {}", updated));

            // Send 'ServiceRequest' to Game module.
            serviceRequest.setLinkedJobId(jobId);
            rabbitTemplate.convertAndSend("myexchange1", "MyQ1", serviceRequest);
        } else {
            final ServiceRequestStatus serviceRequestStatus = new ServiceRequestStatus(
                    jobId,
                    REJECTED,
                    amountOfMoneyToBlock,
                    userId,
                    gameStrategyTypes,
                    currentTimeInMilliseconds,
                    currentTimeInMilliseconds
            );
            serviceRequestStatusRepository.save(serviceRequestStatus).subscribe(updated -> LOGGER.info("Saved : {}", updated));
        }

    }

    private int getAmountOfMoneyWon(final List<Boolean> listOfIsWinner, final int amountGambled) {
        int totalAmountWon = 0;
        for (var i = 0; i < listOfIsWinner.size(); i++) {
            if (Boolean.TRUE.equals(listOfIsWinner.get(i))) {
                totalAmountWon += amountGambled * WIN_MULTIPLIER;
            }
        }
        return totalAmountWon;
    }

    /**
     * <p>This method update the status of the selected 'serviceRequestStatus' line in Cassandra and give money to the user if he/she won the game.</p>
     * <p>For now, when a user wins a game round, the amount gambled is <b>DOUBLED</b> for the said round.</p>
     *
     * @param jobId
     * @param isWinner
     * @param amountGambled
     */
    public void updateServiceRequestStatusByJobIdAndUpdatePlayerMoney(final int jobId, final List<Boolean> listOfIsWinner, final int amountGambled) {
        final ServiceRequestStatus serviceRequestStatus = serviceRequestStatusRepository.findById(jobId).block();

        if (serviceRequestStatus == null) {
            throw new IllegalStateException("ServiceRequestStatus for jobId " + jobId + " do not exists.");
        }

        final int userId = serviceRequestStatus.getUserId();

        serviceRequestStatus.setAckTimeMilliSecond(randomService.getCurrentTimeInSeconds());

        final int amountBlocked = serviceRequestStatus.getAmountBlocked();
        final int totalAmountWon = getAmountOfMoneyWon(listOfIsWinner, amountGambled);

        final UserMoney userMoney = userMoneyRepository.findById(userId).block();

        if (userMoney == null) {
            throw new IllegalStateException("UserMoney for userId " + userId + " do not exists.");
        }

        final int totalAmountOfMoneyAvailable = userMoney.getMoney();

        if (totalAmountWon > amountBlocked) {
            serviceRequestStatus.setStatus(DONE_WIN);
        } else {
            serviceRequestStatus.setStatus(DONE_LOSS);
        }

        userMoney.setMoney(totalAmountOfMoneyAvailable + totalAmountWon);
        userMoneyRepository.save(userMoney).subscribe(updated -> LOGGER.info("Updated : {}", updated));
        serviceRequestStatusRepository.save(serviceRequestStatus).subscribe(updated -> LOGGER.info("Updated : {}", updated));
    }
}
