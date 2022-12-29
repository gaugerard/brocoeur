package brocoeur.example.common;

import java.util.List;

public interface OfflineGameStrategy {

    /**
     * Method for determining the player's action for current game round <b>based on</b> the player's previous win streak.
     *
     * @return GamePlay
     */
    public Gamble playOffline(int availableAmount, List<Boolean> listOfPreviousIsWinner);

    public Gamble playSingleOrMultiple(int availableAmount, List<Boolean> listOfPreviousIsWinner);
}
