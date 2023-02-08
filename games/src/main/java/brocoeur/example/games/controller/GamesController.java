package brocoeur.example.games.controller;

import brocoeur.example.common.ServiceRequestTypes;
import brocoeur.example.common.request.AnalyticServiceRequest;
import brocoeur.example.common.request.PlayerResponse;
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
            case MULTIPLAYER -> processMultiplayerMsg(serviceRequest);
        }
    }

    private void processMultiplayerMsg(final ServiceRequest serviceRequest) {
        LOGGER.info("[MULTIPLAYER] - request : {}", serviceRequest);

        if (serviceRequest.getPlayerRequestList().size() != 3) {
            throw new IllegalStateException("ServiceRequest.PlayerRequestList must be of size 3 (for Poker).");
        }
        final List<PlayerResponse> playerResponseList = gameService.playMultiplayerGame(serviceRequest);
        final AnalyticServiceRequest analyticServiceRequest = new AnalyticServiceRequest(MONEY_MANAGEMENT, playerResponseList);

        rabbitTemplate.convertAndSend(
                gamesConfigProperties.getRpcExchange(),
                gamesConfigProperties.getAnalyticInputQueueName(),
                analyticServiceRequest);
    }

    private void processMsg(final ServiceRequest serviceRequest) {
        LOGGER.info("[SINGLE_PLAYER] - request : {}", serviceRequest);

        if (serviceRequest.getPlayerRequestList().size() != 1) {
            throw new IllegalStateException("ServiceRequest.PlayerRequestList must be of size 1.");
        }
        final List<PlayerResponse> playerResponseList = gameService.playGame(serviceRequest);
        final AnalyticServiceRequest analyticServiceRequest = new AnalyticServiceRequest(MONEY_MANAGEMENT, playerResponseList);

        rabbitTemplate.convertAndSend(
                gamesConfigProperties.getRpcExchange(),
                gamesConfigProperties.getAnalyticInputQueueName(),
                analyticServiceRequest);
    }
}
