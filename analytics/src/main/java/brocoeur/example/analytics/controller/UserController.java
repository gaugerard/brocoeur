package brocoeur.example.controller;

import brocoeur.example.model.User;
import brocoeur.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    ResponseEntity<User> getAllUsers(){
        return new ResponseEntity(userRepository.getAllUsers(), HttpStatus.OK);
    }

}


