package brocoeur.example.nerima.controller;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ServiceResponse)) {
            return false;
        }

        ServiceResponse c = (ServiceResponse) obj;

        return userId.equals(c.userId) && Boolean.compare(isWinner, c.isWinner) == 0;
    }
}
