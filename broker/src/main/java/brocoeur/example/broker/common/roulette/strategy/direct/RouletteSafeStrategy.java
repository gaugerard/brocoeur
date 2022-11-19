package brocoeur.example.broker.common.roulette.strategy.direct;

import brocoeur.example.broker.common.GameStrategy;
import brocoeur.example.broker.common.roulette.RoulettePlay;

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
