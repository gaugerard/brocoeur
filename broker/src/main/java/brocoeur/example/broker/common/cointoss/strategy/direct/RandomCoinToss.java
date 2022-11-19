package brocoeur.example.broker.common.cointoss.strategy.direct;

import brocoeur.example.broker.common.cointoss.CoinTossPlay;
import brocoeur.example.broker.common.GameStrategy;

import java.util.Arrays;
import java.util.Random;

public class RandomCoinToss implements GameStrategy {
    @Override
    public CoinTossPlay getStrategyPlay() {
        return Arrays.stream(CoinTossPlay.values()).toList().get(new Random().nextInt(CoinTossPlay.values().length));
    }

    @Override
    public String toString(){
        return "RandomCoinToss";
    }
}
