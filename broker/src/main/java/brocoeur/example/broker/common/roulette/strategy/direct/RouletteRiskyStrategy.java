package brocoeur.example.broker.common.roulette.strategy.direct;

import brocoeur.example.broker.common.GameStrategy;
import brocoeur.example.broker.common.roulette.RoulettePlay;

public class RouletteRiskyStrategy implements GameStrategy {
    @Override
    public RoulettePlay getStrategyPlay() {
        return RoulettePlay.GREEN;
    }

    @Override
    public String toString(){
        return "RouletteRiskyStrategy";
    }
}
