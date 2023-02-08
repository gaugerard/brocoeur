package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.User;
import brocoeur.example.analytics.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public void initializeGames(List<User> users) {
        userRepository.saveAll(users).subscribe(updated -> LOGGER.info("Initializing : {}", updated));
    }

    public void deleteAllUsers() {
        userRepository.deleteAll().subscribe();
    }
}
