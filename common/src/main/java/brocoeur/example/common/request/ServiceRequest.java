package brocoeur.example.common.request;

import brocoeur.example.common.ServiceRequestTypes;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Component
public class ServiceRequest implements Serializable {

    private ServiceRequestTypes serviceRequestTypes;
    private List<PlayerRequest> playerRequestList;
    private Integer timeToLive;

    public ServiceRequest(final ServiceRequestTypes serviceRequestTypes,
                          final List<PlayerRequest> playerRequestList,
                          final Integer timeToLive) {
        this.serviceRequestTypes = serviceRequestTypes;
        this.playerRequestList = playerRequestList;
        this.timeToLive = timeToLive;
    }

    public ServiceRequest(final ServiceRequestTypes serviceRequestTypes,
                          final PlayerRequest playerRequestList,
                          final Integer timeToLive) {
        this.serviceRequestTypes = serviceRequestTypes;
        this.playerRequestList = List.of(playerRequestList);
        this.timeToLive = timeToLive;
    }


    public ServiceRequest() {
    }

    public ServiceRequest(final ServiceRequest serviceRequest) {
        this.serviceRequestTypes = serviceRequest.serviceRequestTypes;
        this.playerRequestList = List.copyOf(serviceRequest.playerRequestList);
        this.timeToLive = serviceRequest.timeToLive;
    }

    public ServiceRequestTypes getServiceRequestTypes() {
        return serviceRequestTypes;
    }

    public void setServiceRequestTypes(ServiceRequestTypes serviceRequestTypes) {
        this.serviceRequestTypes = serviceRequestTypes;
    }

    public List<PlayerRequest> getPlayerRequestList() {
        return playerRequestList;
    }

    public void setPlayerRequestList(List<PlayerRequest> playerRequestList) {
        this.playerRequestList = playerRequestList;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "serviceRequestTypes='" + serviceRequestTypes + '\'' +
                ", playerRequestList='" + playerRequestList + '\'' +
                ", timeToLive='" + timeToLive + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ServiceRequest)) {
            return false;
        }

        ServiceRequest c = (ServiceRequest) obj;

        return serviceRequestTypes.equals(c.serviceRequestTypes) &&
                Objects.equals(playerRequestList, c.playerRequestList)
                && Objects.equals(timeToLive, c.timeToLive);
    }
}
