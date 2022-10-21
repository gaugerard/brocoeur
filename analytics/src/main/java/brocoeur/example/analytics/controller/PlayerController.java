package brocoeur.example.analytics.controller;

import brocoeur.example.analytics.model.Player;
import brocoeur.example.analytics.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    @GetMapping("/player")
    ResponseEntity<Player> getAllPlayers(){
        return new ResponseEntity(playerRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    ResponseEntity<Player> getPlayerById(@PathVariable(value = "id") Long id){
        return new ResponseEntity(playerRepository.findById(id),HttpStatus.OK);
    }

    @GetMapping("/players/id")
    ResponseEntity<Player> getPlayerById(@RequestBody List<Long> ids){
        return new ResponseEntity(playerRepository.findAllById(ids),HttpStatus.OK);
    }

    @PostMapping("/players")
    ResponseEntity createPlayer(@RequestBody Player player){
        return new ResponseEntity(playerRepository.save(player),HttpStatus.CREATED);
    }

    @DeleteMapping("/players/{id}")
    ResponseEntity deletePlayer(@PathVariable(value = "id") Long id){
        playerRepository.deleteById(id);
        return new ResponseEntity(null,HttpStatus.OK) ;
    }
}


