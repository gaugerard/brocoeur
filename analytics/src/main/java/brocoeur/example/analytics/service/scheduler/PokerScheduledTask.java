package brocoeur.example.analytics.service.scheduler;

import brocoeur.example.analytics.AnalyticsConfigProperties;
import brocoeur.example.analytics.model.ServiceRequestStatus;
import brocoeur.example.analytics.service.ServiceRequestStatusService;
import brocoeur.example.common.GameStrategyTypes;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static brocoeur.example.analytics.service.ServiceRequestStatusStatus.IN_PROGRESS;
import static brocoeur.example.analytics.service.ServiceRequestStatusStatus.TODO;
import static brocoeur.example.common.GameStrategyTypes.POKER_RANDOM;
import static brocoeur.example.common.ServiceRequestTypes.MULTIPLAYER;

@Component
public class PokerScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PokerScheduledTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private ServiceRequestStatusService serviceRequestStatusService;
    @Autowired
    private AnalyticsConfigProperties analyticsConfigProperties;

    /**
     * Every 30 sec, this task will be triggered in order to execute the poker games.
     * <ul>
     *     <li>1) Fetch 3 Poker requests.</li>
     *     <li>2) If there are enough poker requests, send a poker execution request to the 'Game' module.</li>
     *     <li>3) Else, does nothing and waits 30 seconds before executing again.</li>
     * </ul>
     */
    @Scheduled(fixedRateString = "#{analyticsConfigProperties.getPokerFetchingRate()}")
    public void executeScheduledTask() {
        LOGGER.info("Poker scheduled task started at : {}", dateFormat.format(new Date()));

        // Keep 3 first distinct poker request (3 different player).
        final List<ServiceRequestStatus> listServiceRequestStatus = serviceRequestStatusService.findAllServiceRequestStatusByStrategyAndStatus(POKER_RANDOM.toString(), TODO);
        final List<ServiceRequestStatus> listOfServiceRequestStatusToBePlayed = getThreeFirstDistinctServiceRequest(listServiceRequestStatus);

        if (listOfServiceRequestStatusToBePlayed.size() == 3) {
            LOGGER.info("List of poker request to be played: {}", listOfServiceRequestStatusToBePlayed);

            // Generate a MULTIPLAYER ServiceRequest from the 3 poker request.
            final ServiceRequest serviceRequest = createServiceRequest(listOfServiceRequestStatusToBePlayed);
            LOGGER.info("Generated ServiceRequest for poker: {}", serviceRequest);

            // Update Cassandra DB and sends the ServiceRequest.
            serviceRequestStatusService.sendMultiplayerServiceRequest(serviceRequest);
        } else {
            LOGGER.info("Not enough player to play a poker game, waiting 30 seconds before retrying. Number of poker requests {}", listOfServiceRequestStatusToBePlayed.size());
        }
    }

    private List<ServiceRequestStatus> getThreeFirstDistinctServiceRequest(final List<ServiceRequestStatus> listServiceRequestStatus) {
        final List<Integer> listOfUniqueUserId = new ArrayList<>();
        final List<ServiceRequestStatus> listOfServiceRequestStatusToBePlayed = new ArrayList<>();

        for (ServiceRequestStatus serviceRequestStatus : listServiceRequestStatus) {
            final int userId = serviceRequestStatus.getUserId();
            if (listOfServiceRequestStatusToBePlayed.size() < 3 && !listOfUniqueUserId.contains(userId)) {
                listOfUniqueUserId.add(userId);
                listOfServiceRequestStatusToBePlayed.add(serviceRequestStatus);
            }
        }
        return listOfServiceRequestStatusToBePlayed;
    }

    private ServiceRequest createServiceRequest(final List<ServiceRequestStatus> serviceRequestStatusList) {
        final List<PlayerRequest> playerRequestList = new ArrayList<>();

        for (ServiceRequestStatus serviceRequestStatus : serviceRequestStatusList) {
            final String userId = Integer.toString(serviceRequestStatus.getUserId());

            final String strategy = serviceRequestStatus.getStrategy();
            final GameStrategyTypes gameStrategyTypes = GameStrategyTypes.getGameStrategyTypesFromName(strategy);

            final int amountToGamble = serviceRequestStatus.getAmountBlocked();
            final int linkedJobId = serviceRequestStatus.getJobId();

            final PlayerRequest playerRequest = new PlayerRequest(
                    userId,
                    gameStrategyTypes,
                    amountToGamble,
                    linkedJobId);
            playerRequestList.add(playerRequest);
        }

        return new ServiceRequest(MULTIPLAYER, playerRequestList, null);
    }

    /**
     * <p>Every 30 seconds, this task will be triggered to <b>cancel</b> pending/blocked. </p>
     * <p>A job is considered pending/blocked when its has not been completed after 5 minutes.</p>
     */
    @Scheduled(fixedRateString = "#{analyticsConfigProperties.getBlockedRequestRate()}")
    public void cancelBlockedRequests() {
        final Date currentDate = new Date();
        final long currentTime = currentDate.getTime();
        LOGGER.info("Cleaning scheduled task started at : {}", dateFormat.format(currentDate));

        final List<ServiceRequestStatus> pendingTodoRequests = serviceRequestStatusService.findAllServiceRequestStatusByStatus(TODO);
        final List<ServiceRequestStatus> pendingInProgressRequests = serviceRequestStatusService.findAllServiceRequestStatusByStatus(IN_PROGRESS);
        final List<ServiceRequestStatus> allPendingRequest = Stream.concat(pendingTodoRequests.stream(), pendingInProgressRequests.stream()).toList();

        for (ServiceRequestStatus pendingRequest : allPendingRequest) {
            if (pendingRequest.getInsertionTimeMilliSecond() < currentTime - 300_000) {
                LOGGER.info("Cancelling request : {}", pendingRequest);
                serviceRequestStatusService.cancelServiceRequestStatus(pendingRequest);
            }
        }

    }

}
