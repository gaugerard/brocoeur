package brocoeur.example.common;

import java.util.List;

public interface OfflineGameStrategy {

    /**
     * Method for determining the player's action for current game round <b>based on</b> the player's previous win streak.
     *
     * @return GamePlay
     */
    public GamePlay playOffline(List<Boolean> listOfPreviousIsWinner);
}
