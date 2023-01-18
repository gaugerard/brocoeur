package brocoeur.example.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class DeckOfCardsTest {

    @Test
    void shouldGetADeckOfCards() {
        // Given
        var deckOfCards = new DeckOfCards();

        // When
        var card1 = deckOfCards.getCard();
        var card2 = deckOfCards.getCard();

        // Then
        assertThat(card1, instanceOf(DeckOfCards.Card.class));
        assertThat(card2, instanceOf(DeckOfCards.Card.class));
        assertThat(card1, is(not(equalTo(card2))));
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenTakingMoreThan52CardsFromDeck() {
        // Given
        var deckOfCards = new DeckOfCards();

        for (int i = 0; i < 52; i++) {
            deckOfCards.getCard();
        }

        // When
        var exception = Assertions.assertThrows(IndexOutOfBoundsException.class, deckOfCards::getCard);

        // Then
        Assertions.assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }
}
