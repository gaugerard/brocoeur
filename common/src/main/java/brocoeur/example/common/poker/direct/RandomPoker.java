package brocoeur.example.common.poker.direct;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.poker.PokerPlay;

import java.util.Arrays;
import java.util.Random;

public class RandomPoker implements GameStrategy {
    @Override
    public GamePlay getStrategyPlay() {
        return Arrays.stream(PokerPlay.values()).toList().get(new Random().nextInt(PokerPlay.values().length));
    }

    @Override
    public String toString() {
        return "RandomPoker";
    }
}
