package brocoeur.example.games.service.blackjack;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameStrategy;
import brocoeur.example.games.service.GameRound;
import org.springframework.stereotype.Service;

@Service
public class BlackJackService implements GameRound {

    @Override
    public GamePlay play(){
        // Romain code to do here for the dealer to play
        // will return a BlackJackResult => see enum class

        return BlackJackResults.SEVENTEEN;

    }

    @Override
    public GamePlay play(GameStrategy gameStrategy){
        // Romain code to do here for the player to play
        // will return a BlackJackResult => see enum class


        return BlackJackResults.EIGHTEEN;
    }

    public boolean didPlayerWin(final GamePlay userPlay, final GamePlay servicePlay){
        // TODO

        return true;
    }

}
