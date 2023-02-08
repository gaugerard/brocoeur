package brocoeur.example.common;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DeckOfCards {
    private final List<Card> deck = new ArrayList<>();

    public DeckOfCards() {
        for (int i = 0; i < 52; i++) {
            deck.add(new Card(i));
        }
        Collections.shuffle(deck);
    }

    public Card getCard() {
        return deck.remove(0);
    }


    public static class Card {
        private final int value;

        public Card(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Card{" +
                    "value=" + value +
                    '}';
        }
    }
}
