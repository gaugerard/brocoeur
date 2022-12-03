package brocoeur.example.broker.common.request;

import brocoeur.example.broker.common.AnalyticServiceRequestTypes;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class AnalyticServiceRequest implements Serializable {

    private AnalyticServiceRequestTypes analyticServiceRequestTypes;
    private int gameId;
    private int userId;
    private boolean isWinner;
    private int amount;
    private int linkedJobId;

    public AnalyticServiceRequest(final AnalyticServiceRequestTypes analyticServiceRequestTypes,
                                  final int gameId,
                                  final int userId,
                                  final boolean isWinner,
                                  final int amount,
                                  final int linkedJobId) {
        this.analyticServiceRequestTypes = analyticServiceRequestTypes;
        this.gameId = gameId;
        this.userId = userId;
        this.isWinner = isWinner;
        this.amount = amount;
        this.linkedJobId = linkedJobId;
    }

    public AnalyticServiceRequest() {
    }

    public AnalyticServiceRequestTypes getAnalyticServiceRequestTypes() {
        return analyticServiceRequestTypes;
    }

    public void setAnalyticServiceRequestTypes(AnalyticServiceRequestTypes analyticServiceRequestTypes) {
        this.analyticServiceRequestTypes = analyticServiceRequestTypes;
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
        return "AnalyticServiceRequest{" +
                "analyticServiceRequestTypes=" + analyticServiceRequestTypes +
                ", gameId=" + gameId +
                ", userId=" + userId +
                ", isWinner=" + isWinner +
                ", amount=" + amount +
                ", linkedJobId=" + linkedJobId +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof AnalyticServiceRequest)) {
            return false;
        }

        AnalyticServiceRequest c = (AnalyticServiceRequest) obj;

        return analyticServiceRequestTypes.equals(c.analyticServiceRequestTypes) &&
                gameId == c.gameId
                && userId == c.userId
                && isWinner == c.isWinner
                && amount == c.amount
                && linkedJobId == c.linkedJobId;
    }
}
