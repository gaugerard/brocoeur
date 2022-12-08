package brocoeur.example.games.service;

import brocoeur.example.common.GamePlay;
import brocoeur.example.common.GameStrategy;

public interface GameRound {

    public GamePlay play();

    public GamePlay play(GameStrategy gameStrategy);

}
