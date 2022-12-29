package brocoeur.example.games.service.blackjack;

import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.request.ServiceRequest;
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

    public List<PlayerResponse> playBlackJackGame(final ServiceRequest serviceRequest) {
        LOGGER.info("Game of black-jack started for : {}", serviceRequest);

        final PlayerRequest playerRequest = serviceRequest.getPlayerRequestList().get(0);
        final PlayerResponse playerResponse = americanBlackjack.play(playerRequest);

        LOGGER.info("Game of black-jack ended with response : {}", playerResponse);

        return List.of(playerResponse);
    }

}
