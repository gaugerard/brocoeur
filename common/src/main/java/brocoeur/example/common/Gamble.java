package brocoeur.example.common;

import java.util.List;

public class Gamble {
    private final List<GamePlay> gamePlayList;
    private final List<Integer> amountList;

    public Gamble(List<GamePlay> gamePlayList, List<Integer> amountList) {
        this.gamePlayList = gamePlayList;
        this.amountList = amountList;
    }

    public Gamble(GamePlay gamePlay, Integer amount) {
        this.gamePlayList = List.of(gamePlay);
        this.amountList = List.of(amount);
    }

    public List<GamePlay> getGamePlayList() {
        return gamePlayList;
    }

    public List<Integer> getAmountList() {
        return amountList;
    }

    public GamePlay getFirstGamePlay() {
        return gamePlayList.get(0);
    }

    public Integer getFirstAmount() {
        return amountList.get(0);
    }
}
