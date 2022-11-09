package brocoeur.example.nerima.service.offline;

import brocoeur.example.nerima.service.GamePlay;

import java.util.List;

public interface OfflineGameStrategy {

    public GamePlay getOfflineStrategyPlay(List<GamePlay> listOfPreviousGameResul);
}
