package brocoeur.example.games.service.cointoss;


import brocoeur.example.games.service.GameRound;
import brocoeur.example.nerima.service.GamePlay;
import brocoeur.example.nerima.service.cointoss.CoinTossPlay;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class CoinTossService implements GameRound {
    @Override
    public GamePlay play() {
        return Arrays.stream(CoinTossPlay.values()).toList().get(new Random().nextInt(CoinTossPlay.values().length));
    }
}
