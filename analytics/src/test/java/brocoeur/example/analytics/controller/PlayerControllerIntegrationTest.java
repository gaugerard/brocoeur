package brocoeur.example.analytics.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PlayerControllerIntegrationTest {

    @Autowired
    private PlayerController playerController;

    @Test
    void shouldLoadContextForRepositoryTest(){
        //given
        //when
        //then
        assertThat(playerController).isNotNull();
        assertThat(playerController.playerRepository).isNotNull();

    }



}
