package brocoeur.example.analytics.controller;

import brocoeur.example.analytics.model.User;
import brocoeur.example.analytics.service.UserService;
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
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void saveUsers() {
        // Clean-up
        userService.deleteAllUsers();

        // Initialization
        List<User> users = new ArrayList<>();
        users.add(new User(5, "MeatAlive"));
        users.add(new User(8, "Baba"));
        userService.initializeGames(users);
    }

    @GetMapping("/list")
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }
}
