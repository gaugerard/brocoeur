package brocoeur.example.common.blackJack.strategy;

import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.blackJack.BlackJackPlay;

public class BlackJackSafe implements GameStrategy {
    @Override
    public BlackJackPlay getStrategyPlay(){ return BlackJackPlay.STOP_LIMIT_15;}

    @Override
    public String toString() {return "BlackJackSafe";}
}
