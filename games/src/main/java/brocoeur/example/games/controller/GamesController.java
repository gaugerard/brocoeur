package brocoeur.example.games.controller;

import brocoeur.example.games.GamesConfigProperties;
import brocoeur.example.games.service.GameService;
import brocoeur.example.nerima.controller.ServiceRequest;
import brocoeur.example.nerima.controller.ServiceResponse;
import brocoeur.example.nerima.service.GamePlay;
import brocoeur.example.nerima.service.GameStrategy;
import brocoeur.example.nerima.service.GameStrategyTypes;
import brocoeur.example.nerima.service.ServiceRequestTypes;
import brocoeur.example.nerima.service.OfflineGameStrategy;
import brocoeur.example.nerima.service.OfflineGameStrategyTypes;
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
            ServiceResponse serviceResponseWin = new ServiceResponse(userId, true);
            rabbitTemplate.convertSendAndReceive(gamesConfigProperties.getRpcExchange(), gamesConfigProperties.getRpcReplyMessageQueue(), serviceResponseWin, correlationData);
        } else {
            LOGGER.info("User LOST !");
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
