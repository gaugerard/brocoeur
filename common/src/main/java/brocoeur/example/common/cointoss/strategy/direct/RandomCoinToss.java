package brocoeur.example.common.cointoss.strategy.direct;

import brocoeur.example.common.DeckOfCards;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomCoinToss implements GameStrategy {
    @Override
    public CoinTossPlay play() {
        return Arrays.stream(CoinTossPlay.values()).toList().get(new Random().nextInt(CoinTossPlay.values().length));
    }

    @Override
    public CoinTossPlay play(List<DeckOfCards.Card> playerCards) {
        return null;
    }

    @Override
    public CoinTossPlay play(List<DeckOfCards.Card> playerCards, List<DeckOfCards.Card> casinoCards) {
        return null;
    }

    @Override
    public String toString() {
        return "RandomCoinToss";
    }
}
