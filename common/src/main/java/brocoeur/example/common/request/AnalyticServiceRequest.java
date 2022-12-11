package brocoeur.example.common.request;

import brocoeur.example.common.AnalyticServiceRequestTypes;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class AnalyticServiceRequest implements Serializable {

    private AnalyticServiceRequestTypes analyticServiceRequestTypes;
    private int gameId;
    private int userId;
    private List<Boolean> listOfIsWinner;
    private int amount;
    private int linkedJobId;

    public AnalyticServiceRequest(final AnalyticServiceRequestTypes analyticServiceRequestTypes,
                                  final int gameId,
                                  final int userId,
                                  final List<Boolean> listOfIsWinner,
                                  final int amount,
                                  final int linkedJobId) {
        this.analyticServiceRequestTypes = analyticServiceRequestTypes;
        this.gameId = gameId;
        this.userId = userId;
        this.listOfIsWinner = List.copyOf(listOfIsWinner);
        this.amount = amount;
        this.linkedJobId = linkedJobId;
    }

    public AnalyticServiceRequest(final AnalyticServiceRequestTypes analyticServiceRequestTypes,
                                  final int gameId,
                                  final int userId,
                                  final boolean isWinner,
                                  final int amount,
                                  final int linkedJobId) {
        this.analyticServiceRequestTypes = analyticServiceRequestTypes;
        this.gameId = gameId;
        this.userId = userId;
        this.listOfIsWinner = List.of(isWinner);
        this.amount = amount;
        this.linkedJobId = linkedJobId;
    }

    public AnalyticServiceRequest() {
    }

    public AnalyticServiceRequest(final AnalyticServiceRequest analyticServiceRequest) {
        this.analyticServiceRequestTypes = analyticServiceRequest.analyticServiceRequestTypes;
        this.gameId = analyticServiceRequest.gameId;
        this.userId = analyticServiceRequest.userId;
        this.listOfIsWinner = List.copyOf(listOfIsWinner);
        this.amount = analyticServiceRequest.amount;
        this.linkedJobId = analyticServiceRequest.linkedJobId;
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

    public List<Boolean> getListOfIsWinner() {
        return listOfIsWinner;
    }

    public void setListOfIsWinner(final List<Boolean> listOfIsWinner) {
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
        return "AnalyticServiceRequest{" +
                "analyticServiceRequestTypes=" + analyticServiceRequestTypes +
                ", gameId=" + gameId +
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

        if (!(obj instanceof AnalyticServiceRequest)) {
            return false;
        }

        AnalyticServiceRequest c = (AnalyticServiceRequest) obj;

        return analyticServiceRequestTypes.equals(c.analyticServiceRequestTypes) &&
                gameId == c.gameId
                && userId == c.userId
                && listOfIsWinner.equals(c.listOfIsWinner)
                && amount == c.amount
                && linkedJobId == c.linkedJobId;
    }
}
