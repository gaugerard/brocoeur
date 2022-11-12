package brocoeur.example.nerima.service.roulette.strategy.direct;

import brocoeur.example.nerima.service.GameStrategy;
import brocoeur.example.nerima.service.roulette.RoulettePlay;

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
