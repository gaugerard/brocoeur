package brocoeur.example.common.cointoss.strategy;

import brocoeur.example.common.CoinTossGameStrategy;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomCoinTossByHalfAmount implements CoinTossGameStrategy {

    private int getRequestedAmount(final int availableMoney) {
        if (availableMoney == 0) {
            return 0;
        }
        if (availableMoney % 2 == 0) {
            return availableMoney / 2;
        }
        return (availableMoney - 1) / 2;
    }

    @Override
    public Gamble play(int availableMoney, List<CoinTossPlay> previousRoulettePlay) {
        final CoinTossPlay coinTossPlay = Arrays.stream(CoinTossPlay.values()).toList().get(new Random().nextInt(CoinTossPlay.values().length));
        return new Gamble(coinTossPlay, getRequestedAmount(availableMoney));
    }

}
