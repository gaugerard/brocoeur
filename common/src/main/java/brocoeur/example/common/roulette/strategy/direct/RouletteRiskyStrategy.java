package brocoeur.example.common.roulette.strategy.direct;

import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.roulette.RoulettePlay;

public class RouletteRiskyStrategy implements GameStrategy {
    @Override
    public RoulettePlay getStrategy() {
        return RoulettePlay.GREEN;
    }

    @Override
    public String toString(){
        return "RouletteRiskyStrategy";
    }
}
