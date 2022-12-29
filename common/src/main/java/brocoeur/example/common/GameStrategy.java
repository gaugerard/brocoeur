package brocoeur.example.common;

import java.util.List;

public interface GameStrategy {

    /**
     * Legacy way of determining the player's action for current game round.
     *
     * @return GamePlay
     */
    public Gamble play(int availableAmount);

    /**
     * Method for determining the <b>single</b> or <b>multiple</b> player's action(s) for current game round.
     *
     * @return List<GamePlay>
     */
    public Gamble playSingleOrMultiple(int availableAmount);

    /**
     * Method for determining the player's action for current game round <b>based on</b> the player's cards.
     *
     * @return GamePlay
     */
    public Gamble play(List<DeckOfCards.Card> playerCards, int availableAmount);

    /**
     * Method for determining the player's action for current game round <b>based on</b> the player's cards <b>and</b> the casino's cards.
     *
     * @return GamePlay
     */
    public Gamble play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards, int availableAmount);

}
