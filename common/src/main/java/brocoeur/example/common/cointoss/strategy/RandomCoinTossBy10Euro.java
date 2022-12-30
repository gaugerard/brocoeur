package brocoeur.example.common.cointoss.strategy;

import brocoeur.example.common.CoinTossGameStrategy;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomCoinTossBy10Euro implements CoinTossGameStrategy {

    @Override
    public Gamble play(int availableMoney, List<CoinTossPlay> previousRoulettePlay) {
        final CoinTossPlay coinTossPlay = Arrays.stream(CoinTossPlay.values()).toList().get(new Random().nextInt(CoinTossPlay.values().length));

        if (availableMoney >= 10) {
            return new Gamble(coinTossPlay, 10);
        }
        return new Gamble(coinTossPlay, availableMoney);
    }

}
