package brocoeur.example.games.service.cointoss;


import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.cointoss.CoinTossPlay;
import brocoeur.example.games.service.GameRound;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class CoinTossService implements GameRound {
    @Override
    public GamePlay play() {
        return Arrays.stream(CoinTossPlay.values()).toList().get(new Random().nextInt(CoinTossPlay.values().length));
    }
    public GamePlay play(GameStrategy gameStrategy){
        return gameStrategy.getStrategy();
    }

    @Override
    public boolean didPlayerWin(GamePlay userPlay, GamePlay servicePlay){
        return userPlay.equals(servicePlay);
    }

}
