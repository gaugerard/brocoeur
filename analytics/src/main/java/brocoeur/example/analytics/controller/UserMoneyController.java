package brocoeur.example.analytics.controller;

import brocoeur.example.analytics.model.UserMoney;
import brocoeur.example.analytics.service.UserMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("usermoney")
public class UserMoneyController {

    @Autowired
    private UserMoneyService userMoneyService;

    @PostConstruct
    public void saveUserMoney() {
        // Clean-up
        userMoneyService.deleteAllUserMoney();

        // Initialization
        List<UserMoney> userMoneyList = new ArrayList<>();
        userMoneyList.add(new UserMoney(5, 300));
        userMoneyList.add(new UserMoney(8, 1000));
        userMoneyService.initializeUserMoney(userMoneyList);
    }

    @GetMapping("/list")
    public Flux<UserMoney> getAllUserMoney() {
        Flux<UserMoney> UserMoneys = userMoneyService.getAllUserMoney();
        System.out.println(UserMoneys);
        return UserMoneys;
    }

    @GetMapping("/{id}")
    public Mono<UserMoney> getUserMoneyById(@PathVariable int id) {
        return userMoneyService.getUserMoneyById(id);
    }
}
