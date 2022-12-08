package brocoeur.example.common.blackjack.strategy;

import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.blackjack.BlackJackPlay;

public class BlackJackRisky implements GameStrategy {
    @Override
    public BlackJackPlay getStrategy(){ return BlackJackPlay.STOP_LIMIT_20;}

    @Override
    public String toString() {return "BlackJackRisky";}

}
