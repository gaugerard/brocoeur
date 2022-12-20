package brocoeur.example.games.controller;

import brocoeur.example.common.*;
import brocoeur.example.common.request.AnalyticServiceRequest;
import brocoeur.example.common.request.ServiceRequest;
import brocoeur.example.games.GamesConfigProperties;
import brocoeur.example.games.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static brocoeur.example.common.AnalyticServiceRequestTypes.MONEY_MANAGEMENT;

@RestController
public class GamesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamesController.class);

    @Autowired
    private GameService gameService;
    @Autowired
    private GamesConfigProperties gamesConfigProperties;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(id = "game-controller-lister-id", queues = "#{gamesConfigProperties.getRpcMessageQueue()}", autoStartup = "#{gamesConfigProperties.getAutoStartup()}")
    public void getMsg(final ServiceRequest serviceRequest) {
        final ServiceRequestTypes serviceRequestType = serviceRequest.getServiceRequestTypes();
        switch (serviceRequestType) {
            case DIRECT -> processDirectMsg(serviceRequest);
            case OFFLINE -> processOfflineMsg(serviceRequest);
        }
    }

    private void sendAnalyticMessage(final int userId, final GameTypes gameTypes, final boolean isWinner, final int amountToGamble, final int linkedJobId) {
        sendAnalyticMessage(userId, gameTypes, List.of(isWinner), amountToGamble, linkedJobId);
    }

    private void sendAnalyticMessage(final int userId, final GameTypes gameTypes, final List<Boolean> listOfIsWinner, final int amountToGamble, final int linkedJobId) {
        final int gameId = (gameTypes == GameTypes.ROULETTE) ? 123 : 324;
        final AnalyticServiceRequest analyticServiceRequest = new AnalyticServiceRequest(MONEY_MANAGEMENT, gameId, userId, listOfIsWinner, amountToGamble, linkedJobId);
        rabbitTemplate.convertAndSend(
                gamesConfigProperties.getRpcExchange(),
                gamesConfigProperties.getRpcReplyMessageQueue(),
                analyticServiceRequest);
    }

    private void processDirectMsg(final ServiceRequest serviceRequest) {
        LOGGER.info("[DIRECT] - request : {}", serviceRequest);

        final int userId = Integer.parseInt(serviceRequest.getUserId());
        final GameStrategyTypes gameStrategyTypes = serviceRequest.getGameStrategyTypes();
        final GameStrategy gameStrategy = gameStrategyTypes.getGameStrategy();
        final GameTypes gameTypes = gameStrategyTypes.getGameTypes();
        final GamePlay gamePlayFromUser = switch (gameTypes){
            case BLACK_JACK -> gameService.play(gameStrategyTypes.getGameTypes(),gameStrategy);
            default -> gameStrategy.getStrategy();
        };

        final GamePlay gamePlayFromService = gameService.play(gameTypes);



        LOGGER.info("[DIRECT] - USER plays: {} and SERVICE plays: {}", gamePlayFromUser, gamePlayFromService);

        final boolean isWinner = gamePlayFromUser.equals(gamePlayFromService);

        sendAnalyticMessage(userId, gameTypes, isWinner, serviceRequest.getAmountToGamble(), serviceRequest.getLinkedJobId());
    }

    private void processOfflineMsg(final ServiceRequest offlineServiceRequest) {
        LOGGER.info("[OFFLINE] - request : {}", offlineServiceRequest);

        final int userId = Integer.parseInt(offlineServiceRequest.getUserId());
        final OfflineGameStrategyTypes offlineGameStrategyTypes = offlineServiceRequest.getOfflineGameStrategyTypes();
        final OfflineGameStrategy offlineGameStrategy = offlineGameStrategyTypes.getOfflineGameStrategy();
        final int repetition = offlineServiceRequest.getTimeToLive();

        List<GamePlay> listOfPreviousGameResult = new ArrayList<>();
        List<Boolean> listOfIsWinner = new ArrayList<>();

        final GameTypes gameTypes = offlineGameStrategyTypes.getGameTypes();

        for (var i = 0; i < repetition; i++) {
            final GamePlay gamePlayFromUser = offlineGameStrategy.getOfflineStrategyPlay(listOfPreviousGameResult);
            final GamePlay gamePlayFromService = gameService.play(gameTypes);
            LOGGER.info("[OFFLINE] - USER plays: {} and SERVICE plays: {}", gamePlayFromUser, gamePlayFromService);

            listOfPreviousGameResult.add(gamePlayFromService);
            final boolean isWinner = gamePlayFromUser.equals(gamePlayFromService);
            listOfIsWinner.add(isWinner);
        }
        sendAnalyticMessage(userId, gameTypes, listOfIsWinner, offlineServiceRequest.getAmountToGamble(), offlineServiceRequest.getLinkedJobId());
    }
}
