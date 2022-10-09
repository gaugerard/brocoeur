package brocoeur.example.nerima.service.cointoss.strategy;

import brocoeur.example.nerima.service.cointoss.CoinTossPlay;
import brocoeur.example.nerima.service.GameStrategy;

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
