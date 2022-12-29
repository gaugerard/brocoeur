package brocoeur.example.common.cointoss.strategy.direct;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomCoinToss implements GameStrategy {
    @Override
    public Gamble play(int availableAmount) {
        final CoinTossPlay coinTossPlay = Arrays.stream(CoinTossPlay.values()).toList().get(new Random().nextInt(CoinTossPlay.values().length));
        return new Gamble(coinTossPlay, availableAmount);
    }

    @Override
    public Gamble playSingleOrMultiple(int availableAmount) {
        return null;
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
        return "RandomCoinToss";
    }
}
