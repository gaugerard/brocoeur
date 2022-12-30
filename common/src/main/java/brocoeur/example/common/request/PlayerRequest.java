package brocoeur.example.common.request;

import brocoeur.example.common.GameStrategyTypes;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@Component
public class PlayerRequest implements Serializable {
    private String userId;
    private GameStrategyTypes gameStrategyTypes;
    private int amountToGamble;
    private Integer linkedJobId;

    public PlayerRequest() {
    }

    public PlayerRequest(final String userId,
                         final GameStrategyTypes gameStrategyTypes,
                         final int amountToGamble,
                         final Integer linkedJobId) {
        this.userId = userId;
        this.gameStrategyTypes = gameStrategyTypes;
        this.amountToGamble = amountToGamble;
        this.linkedJobId = linkedJobId;
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

    public int getAmountToGamble() {
        return amountToGamble;
    }

    public void setAmountToGamble(int amountToGamble) {
        this.amountToGamble = amountToGamble;
    }

    public Integer getLinkedJobId() {
        return linkedJobId;
    }

    public void setLinkedJobId(Integer linkedJobId) {
        this.linkedJobId = linkedJobId;
    }

    @Override
    public String toString() {
        return "PlayerRequest{" +
                "userId='" + userId + '\'' +
                ", gameStrategyTypes=" + gameStrategyTypes + '\'' +
                ", amountToGamble=" + amountToGamble + '\'' +
                ", linkedJobId=" + linkedJobId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PlayerRequest)) {
            return false;
        }

        PlayerRequest c = (PlayerRequest) obj;

        return Objects.equals(userId, c.userId) &&
                Objects.equals(gameStrategyTypes, c.gameStrategyTypes) &&
                Objects.equals(amountToGamble, c.amountToGamble) &&
                Objects.equals(linkedJobId, c.linkedJobId);
    }
}
