package brocoeur.example.broker.common.response;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class ServiceResponse implements Serializable {

    private String userId;
    private List<Boolean> listOfIsWinner;

    public ServiceResponse(final String userId,
                           final List<Boolean> listOfIsWinner) {
        this.userId = userId;
        this.listOfIsWinner = List.copyOf(listOfIsWinner);
    }

    public ServiceResponse(final String userId,
                           final boolean isWinner) {
        this.userId = userId;
        this.listOfIsWinner = List.of(isWinner);
    }

    public ServiceResponse() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public List<Boolean> getListOfIsWinner() {
        return listOfIsWinner;
    }

    public void setListOfIsWinner(final List<Boolean> listOfIsWinner) {
        this.listOfIsWinner = listOfIsWinner;
    }

    @Override
    public String toString() {
        return "ServiceResponse{" +
                "userId='" + userId + '\'' +
                ", listOfIsWinner='" + listOfIsWinner + '\'' +
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

        return userId.equals(c.userId) && listOfIsWinner.equals(c.listOfIsWinner);
    }
}
