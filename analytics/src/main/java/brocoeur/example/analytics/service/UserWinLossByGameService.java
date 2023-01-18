package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.UserWinLossByGame;
import brocoeur.example.analytics.repository.UserWinLossByGameRepository;
import brocoeur.example.common.request.AnalyticServiceRequest;
import brocoeur.example.common.request.PlayerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserWinLossByGameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserWinLossByGameService.class);

    @Autowired
    private UserWinLossByGameRepository userWinLossByGameRepository;
    @Autowired
    private ServiceRequestStatusService serviceRequestStatusService;

    public void initializeUserWinLossByGame(List<UserWinLossByGame> userWinLossByGames) {
        userWinLossByGameRepository.saveAll(userWinLossByGames).subscribe(updated -> LOGGER.info("Initializing : {}", updated));
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

        final int amountOfMoneyWon = playerResponse.getFinalAmount() - playerResponse.getInitialAmount();
        final int totalAmountOfMoneyWon = userWinLossByGame.getAmountOfMoneyWon();
        final int newTotalAmountOfMoneyWon = totalAmountOfMoneyWon + amountOfMoneyWon;

        final UserWinLossByGame newUserWinLossByGame = new UserWinLossByGame(
                gameId,
                userId,
                userWinLossByGame.getGameName(),
                userWinLossByGame.getPseudo(),
                newTotalAmountOfMoneyWon);

        userWinLossByGameRepository.save(newUserWinLossByGame).subscribe(updated -> LOGGER.info("Updated : {}", updated));
        serviceRequestStatusService.updateServiceRequestStatusByJobIdAndUpdatePlayerMoney(playerResponse.getLinkedJobId(), playerResponse.getFinalAmount());
    }
}
