package nerima.example.roulette.strategy;

import nerima.example.GameStrategy;
import nerima.example.roulette.RoulettePlay;

public class RouletteSafeStrategy implements GameStrategy {
    @Override
    public RoulettePlay play() {
        return RoulettePlay.RED;
    }

    @Override
    public String toString(){
        return "RouletteSafeStrategy";
    }
}
