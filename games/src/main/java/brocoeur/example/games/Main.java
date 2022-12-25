package brocoeur.example.games;

import brocoeur.example.common.DeckOfCards;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }

    @Bean
    public DeckOfCards deckOfCards() {
        return new DeckOfCards();
    }
}