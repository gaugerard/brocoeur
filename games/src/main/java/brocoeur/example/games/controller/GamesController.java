package brocoeur.example.games.controller;

import brocoeur.example.games.GamesConfigProperties;
import brocoeur.example.games.service.GameService;
import brocoeur.example.nerima.controller.OfflineServiceRequest;
import brocoeur.example.nerima.controller.OfflineServiceResponse;
import brocoeur.example.nerima.controller.ServiceRequest;
import brocoeur.example.nerima.controller.ServiceResponse;
import brocoeur.example.nerima.service.GamePlay;
import brocoeur.example.nerima.service.GameStrategy;
import brocoeur.example.nerima.service.GameStrategyTypes;
import brocoeur.example.nerima.service.offline.OfflineGameStrategy;
import brocoeur.example.nerima.service.offline.OfflineGameStrategyTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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

        final String userId = serviceRequest.getUserId();
        final GameStrategyTypes gameStrategyTypes = serviceRequest.getGameStrategyTypes();
        final GameStrategy gameStrategy = gameStrategyTypes.getGameStrategy();
        final GamePlay gamePlayFromUser = gameStrategy.getStrategyPlay();
        LOGGER.info(userId + " used strategy: " + gameStrategy + " and will play: " + gamePlayFromUser);

        final GamePlay gamePlayFromService = gameService.play(serviceRequest.getGameStrategyTypes().getGameTypes());
        LOGGER.info("Servive Games Result is... : " + gamePlayFromService);

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

    @RabbitListener(queues = "#{gamesConfigProperties.getOfflineRpcMessageQueue()}")
    public void getOfflineMsg(final OfflineServiceRequest offlineServiceRequest) {

        final String userId = offlineServiceRequest.getUserId();
        final OfflineGameStrategyTypes offlineGameStrategyTypes = offlineServiceRequest.getOfflineGameStrategyTypes();
        final List<GamePlay> gamePlayList = offlineServiceRequest.getListOfPreviousGameResul();
        final Integer timeToLive = offlineServiceRequest.getTimeToLive();
        LOGGER.info(userId + " used strategy: " + offlineGameStrategyTypes + " gamePlayList: " + gamePlayList + "TTL: " + timeToLive);

        CorrelationData correlationData = new CorrelationData(offlineServiceRequest.getUserId());
        final GamePlay gamePlayFromService = gameService.play(offlineServiceRequest.getOfflineGameStrategyTypes().getGameTypes());

        OfflineServiceResponse offlineServiceResponse = new OfflineServiceResponse(userId, true, gamePlayFromService);
        LOGGER.info("offlineServiceResponse: " + offlineServiceResponse);
        rabbitTemplate.convertSendAndReceive(gamesConfigProperties.getOfflineRpcExchange(), gamesConfigProperties.getOfflineRpcReplyMessageQueue(), offlineServiceResponse, correlationData);
    }
}
