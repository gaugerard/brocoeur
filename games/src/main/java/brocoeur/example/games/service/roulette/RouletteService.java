package brocoeur.example.games.service.roulette;

import brocoeur.example.broker.common.GamePlay;
import brocoeur.example.broker.common.roulette.RoulettePlay;
import brocoeur.example.games.service.GameRound;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class RouletteService implements GameRound {
    @Override
    public GamePlay play() {
        return Arrays.stream(RoulettePlay.values()).toList().get(new Random().nextInt(RoulettePlay.values().length));
    }
}
