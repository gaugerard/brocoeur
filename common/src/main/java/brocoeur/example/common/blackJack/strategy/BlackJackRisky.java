package brocoeur.example.common.blackJack.strategy;

import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.blackJack.BlackJackPlay;

public class BlackJackRisky implements GameStrategy {
    @Override
    public BlackJackPlay getStrategyPlay(){ return BlackJackPlay.STOP_LIMIT_20;}

    @Override
    public String toString() {return "BlackJackRisky";}

}
