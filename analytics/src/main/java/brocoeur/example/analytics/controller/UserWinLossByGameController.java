package brocoeur.example.analytics.controller;

import brocoeur.example.analytics.model.UserWinLossByGame;
import brocoeur.example.analytics.service.UserWinLossByGameService;
import brocoeur.example.common.request.AnalyticServiceRequest;
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

        List<UserWinLossByGame> userWinLossByGameServices = new ArrayList<>();
        userWinLossByGameServices.add(new UserWinLossByGame(123, 5, "Roulette", user5Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(324, 5, "Coin Toss", user5Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(666, 5, "Black Jack", user5Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(123, 8, "Roulette", user8Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(324, 8, "Coin Toss", user8Name, 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(666, 8, "Black Jack", user8Name, 0, 0));

        userWinLossByGameService.initializeUserWinLossByGame(userWinLossByGameServices);
    }

    @RabbitListener(queues = "analyticInput")
    public void getMsg(final AnalyticServiceRequest analyticServiceRequest) {
        final AnalyticServiceRequest copyAnalyticServiceRequest = new AnalyticServiceRequest(analyticServiceRequest);
        userWinLossByGameService.updateAnalyticAccordingToWinOrLoss(copyAnalyticServiceRequest);
    }
}
