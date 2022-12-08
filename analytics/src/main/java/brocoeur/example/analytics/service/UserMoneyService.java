package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.UserMoney;
import brocoeur.example.analytics.repository.UserMoneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class UserMoneyService {
    @Autowired
    private UserMoneyRepository userMoneyRepository;

    public void initializeUserMoney(List<UserMoney> userMoneyList) {
        Flux<UserMoney> savedUserMoney = userMoneyRepository.saveAll(userMoneyList);
        savedUserMoney.subscribe();
    }

    public void deleteAllUserMoney() {
        userMoneyRepository.deleteAll().subscribe();
    }
}
