package brocoeur.example.common.roulette.strategy.direct;

import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.roulette.RoulettePlay;

public class RouletteSafeStrategy implements GameStrategy {
    @Override
    public RoulettePlay getStrategyPlay() {
        return RoulettePlay.RED;
    }

    @Override
    public String toString(){
        return "RouletteSafeStrategy";
    }
}
