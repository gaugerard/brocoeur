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
        String roulette = "Roulette";
        String coinToss = "CoinToss";
        String blackJack = "BlackJack";
        String poker = "Poker";

        List<UserWinLossByGame> userWinLossByGameServices = new ArrayList<>();
        userWinLossByGameServices.add(new UserWinLossByGame(123, 5, roulette, user5Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(324, 5, coinToss, user5Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(666, 5, blackJack, user5Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(420, 5, poker, user5Name, 0, 0));

        userWinLossByGameServices.add(new UserWinLossByGame(123, 8, roulette, user8Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(324, 8, coinToss, user8Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(666, 8, blackJack, user8Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(420, 8, poker, user8Name, 0, 0));

        userWinLossByGameServices.add(new UserWinLossByGame(123, 11, roulette, user11Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(324, 11, coinToss, user11Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(666, 11, blackJack, user11Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(420, 11, poker, user11Name, 0, 0));

        userWinLossByGameService.initializeUserWinLossByGame(userWinLossByGameServices);
    }

    @RabbitListener(queues = "analyticInput")
    public void getMsg(final AnalyticServiceRequest analyticServiceRequest) {
        userWinLossByGameService.manageAnalyticAccordingToWinOrLoss(analyticServiceRequest);
    }
}
