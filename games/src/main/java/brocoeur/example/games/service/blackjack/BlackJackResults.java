package brocoeur.example.games.service.blackjack;

import brocoeur.example.common.GamePlay;
import lombok.Getter;

@Getter
public enum BlackJackResults implements GamePlay {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    ELEVEN(11),
    TWELVE(12),
    THIRTEEN(13),
    FOURTEEN(14),
    FIFTEEN(15),
    SIXTEEN(16),
    SEVENTEEN(17),
    EIGHTEEN(18),
    NINETEEN(19),
    TWENTY(20),
    TWENTY_ONE(21),
    BLACKJACK(22),
    BUST(0);

    private final int value;

    BlackJackResults(final int value){
        this.value = value;
    }
}
