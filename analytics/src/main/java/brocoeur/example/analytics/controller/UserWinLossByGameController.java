package brocoeur.example.analytics.controller;

import brocoeur.example.analytics.model.UserWinLossByGame;
import brocoeur.example.analytics.service.UserWinLossByGameService;
import brocoeur.example.broker.common.request.AnalyticServiceRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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
        List<UserWinLossByGame> userWinLossByGameServices = new ArrayList<>();
        userWinLossByGameServices.add(new UserWinLossByGame(123, 5, "Roulette", "MeatAlive", 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(324, 5, "Coin Toss", "MeatAlive", 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(123, 8, "Roulette", "Baba", 0, 0));
        userWinLossByGameServices.add(new UserWinLossByGame(324, 8, "Coin Toss", "Baba", 0, 0));
        userWinLossByGameService.initializeUserWinLossByGame(userWinLossByGameServices);
    }

    @GetMapping("/list")
    public Flux<UserWinLossByGame> getAllUserWinLossByGame() {
        return userWinLossByGameService.getAllUserWinLossByGame();
    }

    @GetMapping("/{gameid}")
    public Flux<UserWinLossByGame> getUserById(@PathVariable final int gameid) {
        return userWinLossByGameService.getUserWinLossByGameByGameIdAndUserId(gameid, 5);
    }

    @RabbitListener(queues = "analyticInput")
    public void getMsg(final AnalyticServiceRequest analyticServiceRequest) {
        userWinLossByGameService.updateAnalyticAccordingToWinOrLoss(analyticServiceRequest);
    }
}
