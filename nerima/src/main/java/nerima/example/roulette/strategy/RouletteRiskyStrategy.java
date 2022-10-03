package nerima.example.roulette.strategy;

import nerima.example.GameStrategy;
import nerima.example.roulette.RoulettePlay;

public class RouletteRiskyStrategy implements GameStrategy {
    @Override
    public RoulettePlay play() {
        return RoulettePlay.GREEN;
    }

    @Override
    public String toString(){
        return "RouletteRiskyStrategy";
    }
}
