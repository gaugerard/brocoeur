package brocoeur.example.games.service.roulette;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.roulette.RoulettePlay;
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

    public GamePlay play(GameStrategy gameStrategy){
        return gameStrategy.getStrategy();
    }

    @Override
    public boolean didPlayerWin(GamePlay userPlay, GamePlay servicePlay){
        return userPlay.equals(servicePlay);
    }
}
