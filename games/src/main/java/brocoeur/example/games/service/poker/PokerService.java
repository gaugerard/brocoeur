package brocoeur.example.games.service.poker;

import brocoeur.example.common.request.PlayerRequest;
import brocoeur.example.common.request.PlayerResponse;
import brocoeur.example.common.request.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PokerService.class);

    @Autowired
    private TexasHoldemPoker texasHoldemPoker;

    public List<PlayerResponse> playPokerGame(final ServiceRequest serviceRequest) {
        LOGGER.info("Game of poker started for : {}", serviceRequest);

        final PlayerRequest playerRequest1 = serviceRequest.getPlayerRequestList().get(0);
        final PlayerRequest playerRequest2 = serviceRequest.getPlayerRequestList().get(1);
        final PlayerRequest playerRequest3 = serviceRequest.getPlayerRequestList().get(2);

        final List<PlayerResponse> playerResponseList = texasHoldemPoker.play(playerRequest1, playerRequest2, playerRequest3);

        LOGGER.info("Game of poker ended with response : {}", playerResponseList);

        return playerResponseList;
    }
}
