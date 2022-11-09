package brocoeur.example.nerima.controller;

import brocoeur.example.nerima.service.GamePlay;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class OfflineServiceResponse implements Serializable {

    private String userId;
    private boolean isWinner;
    private GamePlay gamePlayFromGame;

    public OfflineServiceResponse(String userId, boolean isWinner, GamePlay gamePlayFromGame) {
        this.userId = userId;
        this.isWinner = isWinner;
        this.gamePlayFromGame = gamePlayFromGame;
    }

    public OfflineServiceResponse() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public GamePlay getGamePlayFromGame() {
        return gamePlayFromGame;
    }

    public void setGamePlayFromGame(GamePlay gamePlayFromGame) {
        this.gamePlayFromGame = gamePlayFromGame;
    }

    @Override
    public String toString() {
        return "OfflineServiceResponse{" +
                "userId='" + userId + '\'' +
                ", isWinner='" + isWinner + '\'' +
                ", gamePlayFromGame='" + gamePlayFromGame + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof OfflineServiceResponse)) {
            return false;
        }

        OfflineServiceResponse c = (OfflineServiceResponse) obj;

        return userId.equals(c.userId) && Boolean.compare(isWinner, c.isWinner) == 0 && gamePlayFromGame.equals(c.gamePlayFromGame);
    }
}
