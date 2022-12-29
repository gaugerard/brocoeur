package brocoeur.example.common.roulette.strategy.direct;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.roulette.RoulettePlay;

import java.util.List;

public class RouletteRiskyStrategy implements GameStrategy {
    @Override
    public Gamble play(int availableAmount) {
        return null;
    }

    @Override
    public Gamble playSingleOrMultiple(int availableAmount) {
        final List<GamePlay> gamePlayList = List.of(RoulettePlay.ZERO, RoulettePlay.TWENTY_SIX, RoulettePlay.THIRTY_TWO);
        final List<Integer> amountList = List.of(availableAmount / 3, availableAmount / 3, availableAmount / 3);
        return new Gamble(gamePlayList, amountList);
    }

    @Override
    public Gamble play(List<DeckOfCards.Card> playerCards, int availableAmount) {
        return null;
    }

    @Override
    public Gamble play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards, int availableAmount) {
        return null;
    }

    @Override
    public String toString() {
        return "RouletteRiskyStrategy";
    }
}
