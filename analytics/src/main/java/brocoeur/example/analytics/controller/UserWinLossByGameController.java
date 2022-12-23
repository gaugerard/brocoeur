package brocoeur.example.analytics.controller;

import brocoeur.example.analytics.model.UserWinLossByGame;
import brocoeur.example.analytics.service.UserWinLossByGameService;
import brocoeur.example.common.request.AnalyticServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("userwinlossbygame")
public class UserWinLossByGameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserWinLossByGameController.class);


    @Autowired
    private UserWinLossByGameService userWinLossByGameService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void saveUsers() {
        // Clean-up
        userWinLossByGameService.deleteAllWinLossByGame();

        // Initialization
        String user5Name = "MeatAlive";
        String user8Name = "Baba";
        String user11Name = "Romain";

        List<UserWinLossByGame> userWinLossByGameServices = new ArrayList<>();
        userWinLossByGameServices.add(new UserWinLossByGame(123, 5, "Roulette", user5Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(324, 5, "Coin Toss", user5Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(666, 5, "Black Jack", user5Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(420, 5, "Poker", user5Name, 0, 0));

        userWinLossByGameServices.add(new UserWinLossByGame(123, 8, "Roulette", user8Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(324, 8, "Coin Toss", user8Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(666, 8, "Black Jack", user8Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(420, 8, "Poker", user8Name, 0, 0));

        userWinLossByGameServices.add(new UserWinLossByGame(123, 11, "Roulette", user11Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(324, 11, "Coin Toss", user11Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(666, 11, "Black Jack", user11Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(420, 11, "Poker", user11Name, 0, 0));

        userWinLossByGameService.initializeUserWinLossByGame(userWinLossByGameServices);
    }

    @RabbitListener(queues = "analyticInput")
    public void getMsg(final AnalyticServiceRequest analyticServiceRequest) {
        userWinLossByGameService.manageAnalyticAccordingToWinOrLoss(analyticServiceRequest);
    }
}
