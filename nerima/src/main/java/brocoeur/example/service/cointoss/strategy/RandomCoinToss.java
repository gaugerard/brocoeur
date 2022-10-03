package brocoeur.example.service.cointoss.strategy;

import brocoeur.example.service.GameStrategy;
import brocoeur.example.service.cointoss.CoinTossPlay;

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
