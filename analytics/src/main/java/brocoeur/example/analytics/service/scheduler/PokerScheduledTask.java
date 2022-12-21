package brocoeur.example.analytics.service.scheduler;

import brocoeur.example.analytics.model.ServiceRequestStatus;
import brocoeur.example.analytics.repository.ServiceRequestStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static brocoeur.example.common.GameStrategyTypes.POKER_RANDOM;

@Component
public class PokerScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PokerScheduledTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private ServiceRequestStatusRepository serviceRequestStatusRepository;

    /**
     * Every 30 sec, this task will be triggered in order to execute the poker games.
     * <ul>
     *     <li>1) Fetch 3 Poker requests.</li>
     *     <li>2) If there are enough poker requests, send a poker execution request to the 'Game' module.</li>
     *     <li>3) Else, does nothing and waits 30 seconds before executing again.</li>
     * </ul>
     */
    @Scheduled(fixedRate = 30000)
    public void executeScheduledTask() {
        LOGGER.info("Poker scheduled task started at : {}", dateFormat.format(new Date()));
        final List<ServiceRequestStatus> listServiceRequestStatus = serviceRequestStatusRepository.findAllByStrategyAndStatus(POKER_RANDOM.toString(), "TODO").collectList().block();
        LOGGER.info("List of poker request : {}", listServiceRequestStatus);
    }
}
