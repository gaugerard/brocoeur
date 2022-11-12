package brocoeur.example.nerima.service.roulette.strategy.direct;

import brocoeur.example.nerima.service.GameStrategy;
import brocoeur.example.nerima.service.roulette.RoulettePlay;

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
