package brocoeur.example.common.request;

import brocoeur.example.common.AnalyticServiceRequestTypes;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class AnalyticServiceRequest implements Serializable {

    private AnalyticServiceRequestTypes analyticServiceRequestTypes;
    private List<PlayerResponse> playerResponseList;

    public AnalyticServiceRequest() {
    }

    public AnalyticServiceRequest(final AnalyticServiceRequestTypes analyticServiceRequestTypes,
                                  final List<PlayerResponse> playerResponseList) {
        this.analyticServiceRequestTypes = analyticServiceRequestTypes;
        this.playerResponseList = playerResponseList;
    }

    public AnalyticServiceRequest(final AnalyticServiceRequestTypes analyticServiceRequestTypes,
                                  final PlayerResponse playerResponseList) {
        this.analyticServiceRequestTypes = analyticServiceRequestTypes;
        this.playerResponseList = List.of(playerResponseList);
    }

    public AnalyticServiceRequest(final AnalyticServiceRequest analyticServiceRequest) {
        this.analyticServiceRequestTypes = analyticServiceRequest.analyticServiceRequestTypes;
        this.playerResponseList = List.copyOf(playerResponseList);
    }

    public AnalyticServiceRequestTypes getAnalyticServiceRequestTypes() {
        return analyticServiceRequestTypes;
    }

    public void setAnalyticServiceRequestTypes(AnalyticServiceRequestTypes analyticServiceRequestTypes) {
        this.analyticServiceRequestTypes = analyticServiceRequestTypes;
    }

    public List<PlayerResponse> getPlayerResponseList() {
        return playerResponseList;
    }

    public void setPlayerResponseList(List<PlayerResponse> playerResponseList) {
        this.playerResponseList = playerResponseList;
    }

    @Override
    public String toString() {
        return "AnalyticServiceRequest{" +
                "analyticServiceRequestTypes=" + analyticServiceRequestTypes +
                ", playerResponseList=" + playerResponseList +
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

        return analyticServiceRequestTypes.equals(c.analyticServiceRequestTypes) && playerResponseList.equals(c.playerResponseList);
    }
}
