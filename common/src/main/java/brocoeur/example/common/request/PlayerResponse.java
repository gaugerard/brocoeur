package brocoeur.example.common.request;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@Component
public class PlayerResponse implements Serializable {
    private int gameId;
    private int userId;
    private int initialAmount;
    private int finalAmount;
    private int linkedJobId;

    public PlayerResponse() {
    }

    public PlayerResponse(final int gameId,
                          final int userId,
                          final int initialAmount,
                          final int finalAmount,
                          final int linkedJobId) {
        this.gameId = gameId;
        this.userId = userId;
        this.initialAmount = initialAmount;
        this.finalAmount = finalAmount;
        this.linkedJobId = linkedJobId;
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

    public int getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(int initialAmount) {
        this.initialAmount = initialAmount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }

    public int getLinkedJobId() {
        return linkedJobId;
    }

    public void setLinkedJobId(int linkedJobId) {
        this.linkedJobId = linkedJobId;
    }

    @Override
    public String toString() {
        return "PlayerResponse{" +
                "gameId=" + gameId +
                ", userId=" + userId +
                ", initialAmount=" + initialAmount +
                ", finalAmount=" + finalAmount +
                ", linkedJobId=" + linkedJobId +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PlayerResponse)) {
            return false;
        }

        PlayerResponse c = (PlayerResponse) obj;

        return Objects.equals(gameId, c.gameId) &&
                Objects.equals(userId, c.userId) &&
                Objects.equals(initialAmount, c.initialAmount) &&
                Objects.equals(finalAmount, c.finalAmount) &&
                Objects.equals(linkedJobId, c.linkedJobId);
    }
}
