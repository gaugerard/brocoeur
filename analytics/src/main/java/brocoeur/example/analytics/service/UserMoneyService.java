package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.UserMoney;
import brocoeur.example.analytics.repository.UserMoneyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMoneyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMoneyService.class);

    @Autowired
    private UserMoneyRepository userMoneyRepository;

    public void initializeUserMoney(List<UserMoney> userMoneyList) {
        userMoneyRepository.saveAll(userMoneyList).subscribe(updated -> LOGGER.info("Initializing : {}", updated));
    }

    public void deleteAllUserMoney() {
        userMoneyRepository.deleteAll().subscribe();
    }
}
