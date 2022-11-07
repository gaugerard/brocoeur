package brocoeur.example.nerima.controller;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@userId", scope = ServiceResponse.class)
public class ServiceResponse implements Serializable {
    private String userId;
    private boolean isWinner;

    public ServiceResponse(String userId, boolean isWinner) {
        this.userId = userId;
        this.isWinner = isWinner;
    }

    public ServiceResponse() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    @Override
    public String toString() {
        return "ServiceResponse{" +
                "userId='" + userId + '\'' +
                ", isWinner='" + isWinner + '\'' +
                '}';
    }
}
