package brocoeur.example.common.request;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Component
public class PlayerResponse implements Serializable {
    private int gameId;
    private int userId;
    private List<Boolean> listOfIsWinner;
    private int amount;
    private int linkedJobId;

    public PlayerResponse() {
    }

    public PlayerResponse(final int gameId,
                          final int userId,
                          final List<Boolean> listOfIsWinner,
                          final int amount,
                          final int linkedJobId) {
        this.gameId = gameId;
        this.userId = userId;
        this.listOfIsWinner = listOfIsWinner;
        this.amount = amount;
        this.linkedJobId = linkedJobId;
    }

    public PlayerResponse(final int gameId,
                          final int userId,
                          final Boolean isWinner,
                          final int amount,
                          final int linkedJobId) {
        this.gameId = gameId;
        this.userId = userId;
        this.listOfIsWinner = List.of(isWinner);
        this.amount = amount;
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

    public List<Boolean> getListOfIsWinner() {
        return listOfIsWinner;
    }

    public void setListOfIsWinner(List<Boolean> listOfIsWinner) {
        this.listOfIsWinner = listOfIsWinner;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
                ", listOfIsWinner=" + listOfIsWinner +
                ", amount=" + amount +
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
                Objects.equals(listOfIsWinner, c.listOfIsWinner) &&
                Objects.equals(amount, c.amount) &&
                Objects.equals(linkedJobId, c.linkedJobId);
    }
}
