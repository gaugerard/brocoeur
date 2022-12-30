package brocoeur.example.games.service.blackjack;

import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlackJackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlackJackService.class);

    @Autowired
    private AmericanBlackjack americanBlackjack;

    public List<PlayerResponse> playBlackJackGame(final PlayerRequest playerRequest, final int ttl) {
        LOGGER.info("Game of black-jack  started for playerRequest: {} with ttl of: {}", playerRequest, ttl);

        final int availableMoney = americanBlackjack.play(playerRequest, ttl);

        final PlayerResponse playerResponse = new PlayerResponse(
                666,
                Integer.parseInt(playerRequest.getUserId()),
                playerRequest.getAmountToGamble(),
                availableMoney,
                playerRequest.getLinkedJobId()
        );

        LOGGER.info("Game of black-jack ended with response : {}", playerResponse);

        return List.of(playerResponse);
    }

}
