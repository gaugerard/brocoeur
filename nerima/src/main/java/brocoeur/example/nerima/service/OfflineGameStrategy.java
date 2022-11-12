package brocoeur.example.nerima.service;

import brocoeur.example.nerima.service.GamePlay;

import java.util.List;

public interface OfflineGameStrategy {

    public GamePlay getOfflineStrategyPlay(List<GamePlay> listOfPreviousGameResult);
}
