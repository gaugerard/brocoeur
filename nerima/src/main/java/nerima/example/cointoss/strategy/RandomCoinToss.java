package nerima.example.cointoss.strategy;

import nerima.example.GameStrategy;
import nerima.example.cointoss.CoinTossPlay;

import java.util.Arrays;
import java.util.Random;

public class RandomCoinToss implements GameStrategy {
    @Override
    public CoinTossPlay play() {
        return Arrays.stream(CoinTossPlay.values()).toList().get(new Random().nextInt(CoinTossPlay.values().length));
    }

    @Override
    public String toString(){
        return "RandomCoinToss";
    }
}
