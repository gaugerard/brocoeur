package brocoeur.example.common.cointoss.strategy;

import brocoeur.example.common.CoinTossGameStrategy;
import brocoeur.example.common.Gamble;
import brocoeur.example.common.cointoss.CoinTossPlay;

import java.util.List;

public class HeadOnlyCoinTossBy5Euro implements CoinTossGameStrategy {

    @Override
    public Gamble play(int availableMoney, List<CoinTossPlay> previousRoulettePlay) {
        if (availableMoney >= 5) {
            return new Gamble(CoinTossPlay.HEAD, 5);
        }
        return new Gamble(CoinTossPlay.HEAD, availableMoney);
    }

}
