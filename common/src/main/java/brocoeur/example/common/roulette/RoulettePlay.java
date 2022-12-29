package brocoeur.example.common.roulette;

import brocoeur.example.common.GamePlay;

public enum RoulettePlay implements GamePlay {
    // Colors
    RED(2),
    BLACK(2),
    // Numbers
    ZERO(36),
    ONE(36),
    TWO(36),
    THREE(36),
    FOUR(36),
    FIVE(36),
    SIX(36),
    SEVEN(36),
    EIGHT(36),
    NINE(36),
    TEN(36),
    ELEVEN(36),
    TWELVE(36),
    THIRTEEN(36),
    FOURTEEN(36),
    FIFTEEN(36),
    SIXTEEN(36),
    SEVENTEEN(36),
    EIGHTEEN(36),
    NINETEEN(36),
    TWENTY(36),
    TWENTY_ONE(36),
    TWENTY_TWO(36),
    TWENTY_THREE(36),
    TWENTY_FOUR(36),
    TWENTY_FIVE(36),
    TWENTY_SIX(36),
    TWENTY_SEVEN(36),
    TWENTY_EIGHT(36),
    TWENTY_NINE(36),
    THIRTY(36),
    THIRTY_ONE(36),
    THIRTY_TWO(36),
    THIRTY_THREE(36),
    THIRTY_FOUR(36),
    THIRTY_FIVE(36),
    THIRTY_SIX(36);

    private final int multiplier;

    RoulettePlay(final int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }
}
