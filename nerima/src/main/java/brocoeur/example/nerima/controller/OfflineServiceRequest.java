package brocoeur.example.nerima.controller;

import brocoeur.example.nerima.service.GamePlay;
import brocoeur.example.nerima.service.offline.OfflineGameStrategyTypes;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfflineServiceRequest implements Serializable {
    private String userId;
    private OfflineGameStrategyTypes offlineGameStrategyTypes;
    private List<GamePlay> listOfPreviousGameResul;
    private Integer timeToLive;

    public OfflineServiceRequest(final String userId, final OfflineGameStrategyTypes offlineGameStrategyTypes, final List<GamePlay> listOfPreviousGameResul, final Integer timeToLive) {
        this.userId = userId;
        this.offlineGameStrategyTypes = offlineGameStrategyTypes;
        this.listOfPreviousGameResul = listOfPreviousGameResul;
        this.timeToLive = timeToLive;
    }

    public OfflineServiceRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OfflineGameStrategyTypes getOfflineGameStrategyTypes() {
        return offlineGameStrategyTypes;
    }

    public void setOfflineGameStrategyTypes(OfflineGameStrategyTypes offlineGameStrategyTypes) {
        this.offlineGameStrategyTypes = offlineGameStrategyTypes;
    }

    public List<GamePlay> getListOfPreviousGameResul() {
        return listOfPreviousGameResul;
    }

    public void setListOfPreviousGameResul(List<GamePlay> listOfPreviousGameResul) {
        this.listOfPreviousGameResul = listOfPreviousGameResul;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Override
    public String toString() {
        return "OfflineServiceRequest{" +
                "userId='" + userId + '\'' +
                ", offlineGameStrategyTypes='" + offlineGameStrategyTypes + '\'' +
                ", listOfPreviousGameResul='" + listOfPreviousGameResul.stream().map(Object::toString).collect(Collectors.joining()) + '\'' +
                ", timeToLive='" + timeToLive + '\'' +
                '}';
    }
}
