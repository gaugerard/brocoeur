package brocoeur.example.analytics.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

@SpringBootTest
public class PlayerControllerTest {

    @Autowired
    private PlayerController playerController;

    @Test
    void shouldTestGetAllUser(){
        //Given

        //When
        var response = playerController.getAllPlayers();

        var status = response.getStatusCode();
        //Then
        Assertions.assertEquals(new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK),response) ;

    }

}
