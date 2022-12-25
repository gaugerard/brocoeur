package brocoeur.example.common;

import java.util.List;

public interface GameStrategy {

    /**
     * Legacy way of determining the player's action for current game round.
     *
     * @return GamePlay
     */
    public GamePlay play();

    /**
     * Method for determining the player's action for current game round <b>based on</b> the player's cards.
     *
     * @return GamePlay
     */
    public GamePlay play(List<DeckOfCards.Card> playerCards);

    /**
     * Method for determining the player's action for current game round <b>based on</b> the player's cards <b>and</b> the casino's cards.
     *
     * @return GamePlay
     */
    public GamePlay play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards);

}
