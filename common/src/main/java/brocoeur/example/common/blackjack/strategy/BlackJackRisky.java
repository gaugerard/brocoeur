package brocoeur.example.common.blackjack.strategy;

import brocoeur.example.common.GameStrategy;
import brocoeur.example.common.blackjack.BlackJackPlay;
import lombok.ToString;

@ToString
public class BlackJackRisky implements GameStrategy {
    @Override
    public BlackJackPlay getStrategy(){ return BlackJackPlay.STOP_LIMIT_20;}


}
