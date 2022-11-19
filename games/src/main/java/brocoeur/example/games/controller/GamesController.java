package brocoeur.example.games.controller;

import brocoeur.example.broker.common.*;
import brocoeur.example.broker.common.request.AnalyticServiceRequest;
import brocoeur.example.broker.common.request.ServiceRequest;
import brocoeur.example.broker.common.response.ServiceResponse;
import brocoeur.example.games.GamesConfigProperties;
import brocoeur.example.games.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    public void sendAnalyticMessage(final int userId, final GameTypes gameTypes, final boolean isWinner) {
        final int gameId = (gameTypes == GameTypes.ROULETTE) ? 123 : 324;
        final AnalyticServiceRequest analyticServiceRequest = new AnalyticServiceRequest(gameId, userId, isWinner);
        rabbitTemplate.convertAndSend("analyticDirectExchange", "analyticInput", analyticServiceRequest);
    }

    private void processDirectMsg(final ServiceRequest serviceRequest) {
        final String userId = serviceRequest.getUserId();
        final GameStrategyTypes gameStrategyTypes = serviceRequest.getGameStrategyTypes();
        final GameStrategy gameStrategy = gameStrategyTypes.getGameStrategy();

        final GamePlay gamePlayFromUser = gameStrategy.getStrategyPlay();
        final GamePlay gamePlayFromService = gameService.play(gameStrategyTypes.getGameTypes());
        LOGGER.info("[DIRECT] - USER plays: '" + gamePlayFromUser + "' and SERVICE plays: '" + gamePlayFromService + "'.");

        CorrelationData correlationData = new CorrelationData(serviceRequest.getUserId());

        if (gamePlayFromUser.equals(gamePlayFromService)) {
            LOGGER.info("User WON !");
            sendAnalyticMessage(Integer.parseInt(userId), gameStrategyTypes.getGameTypes(), true);
            ServiceResponse serviceResponseWin = new ServiceResponse(userId, true);
            rabbitTemplate.convertSendAndReceive(gamesConfigProperties.getRpcExchange(), gamesConfigProperties.getRpcReplyMessageQueue(), serviceResponseWin, correlationData);
        } else {
            LOGGER.info("User LOST !");
            sendAnalyticMessage(Integer.parseInt(userId), gameStrategyTypes.getGameTypes(), false);
            ServiceResponse serviceResponseLost = new ServiceResponse(userId, false);
            rabbitTemplate.convertSendAndReceive(gamesConfigProperties.getRpcExchange(), gamesConfigProperties.getRpcReplyMessageQueue(), serviceResponseLost, correlationData);
        }
    }

    private void processOfflineMsg(final ServiceRequest offlineServiceRequest) {
        final String userId = offlineServiceRequest.getUserId();
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

        CorrelationData correlationData = new CorrelationData(offlineServiceRequest.getUserId());
        ServiceResponse serviceResponseLost = new ServiceResponse(userId, listOfIsWinner);
        rabbitTemplate.convertSendAndReceive(gamesConfigProperties.getRpcExchange(), gamesConfigProperties.getRpcReplyMessageQueue(), serviceResponseLost, correlationData);
    }
}
