package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.UserWinLossByGame;
import brocoeur.example.analytics.repository.UserWinLossByGameRepository;
import brocoeur.example.common.request.AnalyticServiceRequest;
import brocoeur.example.common.request.PlayerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class UserWinLossByGameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserWinLossByGameService.class);

    @Autowired
    private UserWinLossByGameRepository userWinLossByGameRepository;
    @Autowired
    private ServiceRequestStatusService serviceRequestStatusService;

    public void initializeUserWinLossByGame(List<UserWinLossByGame> userWinLossByGames) {
        Flux<UserWinLossByGame> savedUserWinLossByGame = userWinLossByGameRepository.saveAll(userWinLossByGames);
        savedUserWinLossByGame.subscribe();
    }

    public void deleteAllWinLossByGame() {
        userWinLossByGameRepository.deleteAll().subscribe();
    }

    /**
     * Updates Win or Loss for each player involved (may contain several players if MULTIPLAYER game)..
     *
     * @param analyticServiceRequest
     */
    public void manageAnalyticAccordingToWinOrLoss(final AnalyticServiceRequest analyticServiceRequest) {
        for (PlayerResponse playerResponse : analyticServiceRequest.getPlayerResponseList()) {
            updateAnalyticAccordingToWinOrLoss(playerResponse);
        }
    }

    private void updateAnalyticAccordingToWinOrLoss(final PlayerResponse playerResponse) {
        final int gameId = playerResponse.getGameId();
        final int userId = playerResponse.getUserId();
        final UserWinLossByGame userWinLossByGame = userWinLossByGameRepository.findByGameIdAndUserId(gameId, userId).block();

        if (userWinLossByGame == null) {
            throw new IllegalStateException("UserWinLossByGame with gameId : " + gameId + " and userId : " + userId + " do not exists.");
        }

        final List<Boolean> listOfIsWinner = playerResponse.getListOfIsWinner();

        int updatedNumberOfWin = userWinLossByGame.getNumberOfWin();
        int updatedNumberOfLoss = userWinLossByGame.getNumberOfLoss();
        for (Boolean aBoolean : listOfIsWinner) {
            if (Boolean.TRUE.equals(aBoolean)) {
                updatedNumberOfWin += 1;
            } else {
                updatedNumberOfLoss += 1;
            }
        }

        final UserWinLossByGame newUserWinLossByGame = new UserWinLossByGame(
                gameId,
                userId,
                userWinLossByGame.getGameName(),
                userWinLossByGame.getPseudo(),
                updatedNumberOfWin,
                updatedNumberOfLoss);

        userWinLossByGameRepository.save(newUserWinLossByGame).subscribe(updated -> LOGGER.info("Updated : {}", updated));
        serviceRequestStatusService.updateServiceRequestStatusByJobIdAndUpdatePlayerMoney(playerResponse.getLinkedJobId(), listOfIsWinner, playerResponse.getAmount());
    }
}
