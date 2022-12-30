package brocoeur.example.common.cointoss;

import brocoeur.example.common.GamePlay;

public enum CoinTossPlay implements GamePlay {
    HEAD(2),
    TAIL(2);

    private final int multiplier;

    CoinTossPlay(final int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }
}
