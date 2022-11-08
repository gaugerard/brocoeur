package brocoeur.example.nerima.controller;

import brocoeur.example.nerima.service.GameStrategyTypes;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ServiceRequest implements Serializable {
    private String userId;
    private GameStrategyTypes gameStrategyTypes;

    public ServiceRequest(String userId, GameStrategyTypes gameStrategyTypes) {
        this.userId = userId;
        this.gameStrategyTypes = gameStrategyTypes;
    }

    public ServiceRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GameStrategyTypes getGameStrategyTypes() {
        return gameStrategyTypes;
    }

    public void setGameStrategyTypes(GameStrategyTypes gameStrategyTypes) {
        this.gameStrategyTypes = gameStrategyTypes;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "userId='" + userId + '\'' +
                ", gameStrategyTypes='" + gameStrategyTypes + '\'' +
                '}';
    }
}
