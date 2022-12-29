package brocoeur.example.games.service.roulette;

import brocoeur.example.common.Gamble;
import brocoeur.example.common.GamePlay;
import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.roulette.RoulettePlay;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EuropeanRoulette {
    private static final List<RoulettePlay> COLOR_TO_EXCLUDE = List.of(
            RoulettePlay.RED,
            RoulettePlay.BLACK);

    private static final List<RoulettePlay> RED_NUMBERS = List.of(
            RoulettePlay.THIRTY_TWO,
            RoulettePlay.NINETEEN,
            RoulettePlay.TWENTY_ONE,
            RoulettePlay.TWENTY_FIVE,
            RoulettePlay.THIRTY_FOUR,
            RoulettePlay.TWENTY_SEVEN,
            RoulettePlay.THIRTY_SIX,
            RoulettePlay.THIRTY,
            RoulettePlay.TWENTY_THREE,
            RoulettePlay.FIVE,
            RoulettePlay.SIXTEEN,
            RoulettePlay.ONE,
            RoulettePlay.FOURTEEN,
            RoulettePlay.NINE,
            RoulettePlay.EIGHTEEN,
            RoulettePlay.SEVEN,
            RoulettePlay.TWELVE,
            RoulettePlay.THREE);
    private static final List<RoulettePlay> BLACK_NUMBERS = List.of(
            RoulettePlay.FIFTEEN,
            RoulettePlay.FOUR,
            RoulettePlay.TWO,
            RoulettePlay.SEVENTEEN,
            RoulettePlay.SIX,
            RoulettePlay.THIRTEEN,
            RoulettePlay.ELEVEN,
            RoulettePlay.EIGHT,
            RoulettePlay.TEN,
            RoulettePlay.TWENTY_FOUR,
            RoulettePlay.THIRTY_THREE,
            RoulettePlay.TWENTY,
            RoulettePlay.THIRTY_ONE,
            RoulettePlay.TWENTY_TWO,
            RoulettePlay.TWENTY_NINE,
            RoulettePlay.TWENTY_EIGHT,
            RoulettePlay.THIRTY_FIVE,
            RoulettePlay.TWENTY_SIX
    );

    public PlayerResponse play(final PlayerRequest player) {
        final int initialAvailableAmount = player.getAmountToGamble();

        final Gamble gamble = player.getGameStrategyTypes().getGameStrategy().playSingleOrMultiple(initialAvailableAmount);
        final int remainingAvailableAmount = updateAvailableAmount(initialAvailableAmount, gamble);
        final List<RoulettePlay> servicePlay = getServicePlay();

        final int amountWon = remainingAvailableAmount + getAmountWon(gamble, servicePlay);
        final boolean isWinner = amountWon >= initialAvailableAmount;
        return new PlayerResponse(
                123,
                Integer.parseInt(player.getUserId()),
                isWinner,
                amountWon,
                player.getLinkedJobId());
    }

    private int updateAvailableAmount(final int availableAmount, final Gamble gamble) {
        final int amountGambled = gamble.getAmountList().stream().mapToInt(Integer::intValue).sum();
        return availableAmount - amountGambled;
    }

    public PlayerResponse play(final PlayerRequest player, final List<Boolean> listOfIsWinner) {
        final int initialAvailableAmount = player.getAmountToGamble();

        final Gamble gamble = player.getOfflineGameStrategyTypes().getOfflineGameStrategy().playSingleOrMultiple(initialAvailableAmount, listOfIsWinner);
        final int remainingAvailableAmount = updateAvailableAmount(initialAvailableAmount, gamble);
        final List<RoulettePlay> servicePlay = getServicePlay();

        final int amountWon = remainingAvailableAmount + getAmountWon(gamble, servicePlay);
        final boolean isWinner = amountWon >= initialAvailableAmount;
        return new PlayerResponse(
                123,
                Integer.parseInt(player.getUserId()),
                isWinner,
                amountWon,
                player.getLinkedJobId());
    }

    private RoulettePlay play() {
        return Arrays.stream(RoulettePlay.values()).filter(value -> !COLOR_TO_EXCLUDE.contains(value)).toList().get(new Random().nextInt(RoulettePlay.values().length));
    }

    private List<RoulettePlay> getServicePlay() {
        final List<RoulettePlay> servicePlay = new ArrayList<>();

        final RoulettePlay servicePlayNumber = play();
        servicePlay.add(servicePlayNumber);

        Optional.ofNullable(getColorFromNumber(servicePlayNumber)).ifPresent(servicePlay::add);

        return List.copyOf(servicePlay);
    }

    private int getAmountWon(final Gamble gamble, final List<RoulettePlay> servicePlay) {
        final List<GamePlay> gamePlayList = gamble.getGamePlayList();
        final List<Integer> amountList = gamble.getAmountList();

        int amountWon = 0;

        for (int i = 0; i < gamePlayList.size(); i++) {
            final RoulettePlay roulettePlay = (RoulettePlay) gamePlayList.get(i);
            if (servicePlay.contains(roulettePlay)) {
                final int multiplier = roulettePlay.getMultiplier();
                amountWon += (multiplier * amountList.get(i));
            }
        }

        return amountWon;
    }

    private RoulettePlay getColorFromNumber(final RoulettePlay roulettePlayNumber) {
        if (RED_NUMBERS.contains(roulettePlayNumber)) {
            return RoulettePlay.RED;
        }
        if (BLACK_NUMBERS.contains(roulettePlayNumber)) {
            return RoulettePlay.BLACK;
        }
        return null;
    }
}
