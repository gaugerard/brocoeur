package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.UserWinLossByGame;
import brocoeur.example.analytics.repository.UserWinLossByGameRepository;
import brocoeur.example.broker.common.request.AnalyticServiceRequest;
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

    public Flux<UserWinLossByGame> getAllUserWinLossByGame() {
        return userWinLossByGameRepository.findAll();
    }

    public Flux<UserWinLossByGame> getUserWinLossByGameByGameIdAndUserId(final int gameId, final int userId) {
        return userWinLossByGameRepository.findByGameIdAndUserId(gameId, userId);
    }

    public void deleteAllWinLossByGame() {
        userWinLossByGameRepository.deleteAll();
    }

    public void updateWinLossNumber(final AnalyticServiceRequest analyticServiceRequest) {
        final UserWinLossByGame userWinLossByGame = userWinLossByGameRepository.findByGameIdAndUserId(analyticServiceRequest.getGameId(), analyticServiceRequest.getUserId()).blockFirst();
        final int updatedNumberOfWin = analyticServiceRequest.isWinner() ? userWinLossByGame.getNumberOfWin() + 1 : userWinLossByGame.getNumberOfWin();
        final int updatedNumberOfLoss = analyticServiceRequest.isWinner() ? userWinLossByGame.getNumberOfLoss() : userWinLossByGame.getNumberOfLoss() + 1;

        final UserWinLossByGame newUserWinLossByGame = new UserWinLossByGame(
                analyticServiceRequest.getGameId(),
                analyticServiceRequest.getUserId(),
                userWinLossByGame.getGameName(),
                userWinLossByGame.getPseudo(),
                updatedNumberOfWin,
                updatedNumberOfLoss);

        userWinLossByGameRepository.save(newUserWinLossByGame).subscribe(updated -> LOGGER.info("==> " + userWinLossByGame + " UPDATED TO: " + updated));
        serviceRequestStatusService.updateServiceRequestStatusByJobId(analyticServiceRequest.getLinkedJobId(), analyticServiceRequest.isWinner(), analyticServiceRequest.getAmount());
    }
}
