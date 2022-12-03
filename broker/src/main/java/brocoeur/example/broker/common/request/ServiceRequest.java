package brocoeur.example.broker.common.request;

import brocoeur.example.broker.common.GameStrategyTypes;
import brocoeur.example.broker.common.OfflineGameStrategyTypes;
import brocoeur.example.broker.common.ServiceRequestTypes;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static brocoeur.example.broker.common.ServiceRequestTypes.DIRECT;
import static brocoeur.example.broker.common.ServiceRequestTypes.OFFLINE;

@Component
public class ServiceRequest implements Serializable {

    private ServiceRequestTypes serviceRequestTypes;
    private String userId;
    private GameStrategyTypes gameStrategyTypes;
    private OfflineGameStrategyTypes offlineGameStrategyTypes;
    private Integer timeToLive;
    private int amountToGamble;
    private int linkedJobId;

    public ServiceRequest(final ServiceRequestTypes serviceRequestTypes,
                          final String userId,
                          final GameStrategyTypes gameStrategyTypes,
                          final OfflineGameStrategyTypes offlineGameStrategyTypes,
                          final Integer timeToLive,
                          final int amountToGamble,
                          final int linkedJobId) {
        this.serviceRequestTypes = serviceRequestTypes;
        this.userId = userId;
        this.gameStrategyTypes = gameStrategyTypes;
        this.offlineGameStrategyTypes = offlineGameStrategyTypes;
        this.timeToLive = timeToLive;
        this.amountToGamble = amountToGamble;
        this.linkedJobId = linkedJobId;
    }

    public ServiceRequest(final String userId,
                          final GameStrategyTypes gameStrategyTypes,
                          final int amountToGamble) {
        this.serviceRequestTypes = DIRECT;
        this.userId = userId;
        this.gameStrategyTypes = gameStrategyTypes;
        this.offlineGameStrategyTypes = null;
        this.timeToLive = null;
        this.amountToGamble = amountToGamble;
    }

    public ServiceRequest(final String userId,
                          final OfflineGameStrategyTypes offlineGameStrategyTypes,
                          final Integer timeToLive) {
        this.serviceRequestTypes = OFFLINE;
        this.userId = userId;
        this.gameStrategyTypes = null;
        this.offlineGameStrategyTypes = offlineGameStrategyTypes;
        this.timeToLive = timeToLive;
    }

    public ServiceRequest() {
    }

    public ServiceRequestTypes getServiceRequestTypes() {
        return serviceRequestTypes;
    }

    public void setServiceRequestTypes(final ServiceRequestTypes serviceRequestTypes) {
        this.serviceRequestTypes = serviceRequestTypes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public GameStrategyTypes getGameStrategyTypes() {
        return gameStrategyTypes;
    }

    public void setGameStrategyTypes(final GameStrategyTypes gameStrategyTypes) {
        this.gameStrategyTypes = gameStrategyTypes;
    }

    public OfflineGameStrategyTypes getOfflineGameStrategyTypes() {
        return offlineGameStrategyTypes;
    }

    public void setOfflineGameStrategyTypes(final OfflineGameStrategyTypes offlineGameStrategyTypes) {
        this.offlineGameStrategyTypes = offlineGameStrategyTypes;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(final Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getAmountToGamble() {
        return amountToGamble;
    }

    public void setAmountToGamble(int amountToGamble) {
        this.amountToGamble = amountToGamble;
    }

    public int getLinkedJobId() {
        return linkedJobId;
    }

    public void setLinkedJobId(int linkedJobId) {
        this.linkedJobId = linkedJobId;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "serviceRequestTypes='" + serviceRequestTypes + '\'' +
                ", userId='" + userId + '\'' +
                ", gameStrategyTypes='" + gameStrategyTypes + '\'' +
                ", offlineGameStrategyTypes='" + offlineGameStrategyTypes + '\'' +
                ", timeToLive='" + timeToLive + '\'' +
                ", amountToGamble='" + amountToGamble + '\'' +
                ", linkedJobId='" + linkedJobId + '\'' +
                '}';
    }
}
