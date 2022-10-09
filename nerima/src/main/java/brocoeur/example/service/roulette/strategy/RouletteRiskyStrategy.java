package brocoeur.example.service.roulette.strategy;

import brocoeur.example.service.GameStrategy;
import brocoeur.example.service.roulette.RoulettePlay;

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
