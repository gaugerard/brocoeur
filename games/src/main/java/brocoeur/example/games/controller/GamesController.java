package brocoeur.example.games.controller;

import brocoeur.example.broker.common.*;
import brocoeur.example.broker.common.request.AnalyticServiceRequest;
import brocoeur.example.broker.common.request.ServiceRequest;
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

import static brocoeur.example.broker.common.AnalyticServiceRequestTypes.MONEY_MANAGEMENT;

@RestController
public class GamesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamesController.class);

    @Autowired
    private GameService gameService;
    @Autowired
    private GamesConfigProperties gamesConfigProperties;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "#{gamesConfigProperties.getRpcMessageQueue()}")
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
        final int userId = Integer.parseInt(serviceRequest.getUserId());
        final GameStrategyTypes gameStrategyTypes = serviceRequest.getGameStrategyTypes();
        final GameStrategy gameStrategy = gameStrategyTypes.getGameStrategy();

        final GamePlay gamePlayFromUser = gameStrategy.getStrategyPlay();
        final GamePlay gamePlayFromService = gameService.play(gameStrategyTypes.getGameTypes());
        LOGGER.info("[DIRECT] - USER plays: '" + gamePlayFromUser + "' and SERVICE plays: '" + gamePlayFromService + "'.");

        if (gamePlayFromUser.equals(gamePlayFromService)) {
            LOGGER.info("User WON !");
            sendAnalyticMessage(userId, gameStrategyTypes.getGameTypes(), true, serviceRequest.getAmountToGamble(), serviceRequest.getLinkedJobId());
        } else {
            LOGGER.info("User LOST !");
            sendAnalyticMessage(userId, gameStrategyTypes.getGameTypes(), false, serviceRequest.getAmountToGamble(), serviceRequest.getLinkedJobId());
        }
    }

    private void processOfflineMsg(final ServiceRequest offlineServiceRequest) {
        LOGGER.info("[OFFLINE] " + offlineServiceRequest);
        final int userId = Integer.parseInt(offlineServiceRequest.getUserId());
        final OfflineGameStrategyTypes offlineGameStrategyTypes = offlineServiceRequest.getOfflineGameStrategyTypes();
        final OfflineGameStrategy offlineGameStrategy = offlineGameStrategyTypes.getOfflineGameStrategy();
        final int repetition = offlineServiceRequest.getTimeToLive();

        List<GamePlay> listOfPreviousGameResult = new ArrayList<>();
        List<Boolean> listOfIsWinner = new ArrayList<>();

        for (var i = 0; i < repetition; i++) {
            final GamePlay gamePlayFromUser = offlineGameStrategy.getOfflineStrategyPlay(listOfPreviousGameResult);
            final GamePlay gamePlayFromService = gameService.play(offlineGameStrategyTypes.getGameTypes());
            LOGGER.info("[OFFLINE] - USER plays: '" + gamePlayFromUser + "' and SERVICE plays: '" + gamePlayFromService + "'.");

            listOfPreviousGameResult.add(gamePlayFromService);

            if (gamePlayFromUser.equals(gamePlayFromService)) {
                LOGGER.info("User WON !");
                listOfIsWinner.add(true);
            } else {
                LOGGER.info("User LOST !");
                listOfIsWinner.add(false);
            }
        }
        sendAnalyticMessage(userId, offlineGameStrategyTypes.getGameTypes(), listOfIsWinner, offlineServiceRequest.getAmountToGamble(), offlineServiceRequest.getLinkedJobId());
    }
}
