package brocoeur.example.service.roulette.strategy;

import brocoeur.example.service.GameStrategy;
import brocoeur.example.service.roulette.RoulettePlay;

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
