package brocoeur.example.analytics.service;

import brocoeur.example.analytics.model.User;
import brocoeur.example.analytics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void initializeGames(List<User> users) {
        Flux<User> savedUsers = userRepository.saveAll(users);
        savedUsers.subscribe();
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(int id) {
        return userRepository.findById(id);
    }
}
