package brocoeur.example.nerima.controller;

import brocoeur.example.nerima.service.GameStrategyTypes;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@userId", scope = ServiceRequest.class)
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
