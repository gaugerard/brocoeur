package brocoeur.example.games.service.roulette;

import brocoeur.example.nerima.service.GamePlay;
import brocoeur.example.games.service.GameRound;
import brocoeur.example.nerima.service.roulette.RoulettePlay;

import java.util.Arrays;
import java.util.Random;

public class RouletteService implements GameRound {
    @Override
    public GamePlay play() {
        return Arrays.stream(RoulettePlay.values()).toList().get(new Random().nextInt(RoulettePlay.values().length));
    }
}
