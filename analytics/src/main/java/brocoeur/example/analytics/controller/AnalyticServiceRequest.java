package brocoeur.example.analytics.controller;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class AnalyticServiceRequest implements Serializable {

    private int gameId;
    private int userId;
    private boolean isWinner;

    public AnalyticServiceRequest(int gameId, int userId, boolean isWinner) {
        this.gameId = gameId;
        this.userId = userId;
        this.isWinner = isWinner;
    }

    public AnalyticServiceRequest() {
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    @Override
    public String toString() {
        return "AnalyticServiceRequest{" +
                "gameId=" + gameId +
                ", userId=" + userId +
                ", isWinner=" + isWinner +
                '}';
    }
}
