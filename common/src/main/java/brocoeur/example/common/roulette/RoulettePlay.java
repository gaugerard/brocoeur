package brocoeur.example.common.roulette;

import brocoeur.example.common.GamePlay;

public enum RoulettePlay implements GamePlay {
    // Colors
    RED(2, "RED"),
    BLACK(2, "BLACK"),
    // Numbers
    ZERO(36, "GREEN"),
    ONE(36, "RED"),
    TWO(36, "BLACK"),
    THREE(36, "RED"),
    FOUR(36, "BLACK"),
    FIVE(36, "RED"),
    SIX(36, "BLACK"),
    SEVEN(36, "RED"),
    EIGHT(36, "BLACK"),
    NINE(36, "RED"),
    TEN(36, "BLACK"),
    ELEVEN(36, "BLACK"),
    TWELVE(36, "RED"),
    THIRTEEN(36, "BLACK"),
    FOURTEEN(36, "RED"),
    FIFTEEN(36, "BLACK"),
    SIXTEEN(36, "RED"),
    SEVENTEEN(36, "BLACK"),
    EIGHTEEN(36, "RED"),
    NINETEEN(36, "RED"),
    TWENTY(36, "BLACK"),
    TWENTY_ONE(36, "RED"),
    TWENTY_TWO(36, "BLACK"),
    TWENTY_THREE(36, "RED"),
    TWENTY_FOUR(36, "BLACK"),
    TWENTY_FIVE(36, "RED"),
    TWENTY_SIX(36, "BLACK"),
    TWENTY_SEVEN(36, "RED"),
    TWENTY_EIGHT(36, "BLACK"),
    TWENTY_NINE(36, "BLACK"),
    THIRTY(36, "RED"),
    THIRTY_ONE(36, "BLACK"),
    THIRTY_TWO(36, "RED"),
    THIRTY_THREE(36, "BLACK"),
    THIRTY_FOUR(36, "RED"),
    THIRTY_FIVE(36, "BLACK"),
    THIRTY_SIX(36, "RED");

    private final int multiplier;
    private final String color;

    RoulettePlay(final int multiplier, final String color) {
        this.multiplier = multiplier;
        this.color = color;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public String getColor() {
        return color;
    }
}
