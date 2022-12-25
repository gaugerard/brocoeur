package brocoeur.example.common.roulette.strategy.direct;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.roulette.RoulettePlay;

import java.util.List;

public class RouletteSafeStrategy implements GameStrategy {
    @Override
    public RoulettePlay play() {
        return RoulettePlay.RED;
    }

    @Override
    public RoulettePlay play(List<DeckOfCards.Card> playerCards) {
        return null;
    }

    @Override
    public RoulettePlay play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards) {
        return null;
    }

    @Override
    public String toString() {
        return "RouletteSafeStrategy";
    }
}
