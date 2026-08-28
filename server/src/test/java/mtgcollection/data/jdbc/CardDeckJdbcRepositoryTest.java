package mtgcollection.data.jdbc;

import mtgcollection.model.CardDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CardDeckJdbcRepositoryTest {

    @Autowired
    CardDeckJdbcRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void runSetKnownGoodState(){
        jdbcClient.sql("call set_known_good_state();").update();
    }


    @Nested
    class FetchAllCardDecks{
        @Test
        void shouldFetchAllCardDecks(){
            int validDeckId = 1;
            int numberOfCardsInDeck = 3;
            List<CardDeck> cardDecks = repository.fetchAllCardDecksFromDeckId(validDeckId);
            assertEquals(numberOfCardsInDeck,cardDecks.size());
        }
        @Test
        void shouldNotFetchAllCardDecksWhereDeckDoesNotExist(){
            int invalidDeckId = Integer.MAX_VALUE;
            List<CardDeck> cardDecks = repository.fetchAllCardDecksFromDeckId(invalidDeckId);
            assertEquals(0,cardDecks.size());
        }
    }

    @Nested
    class AddCardDeck{
        @Test
        void shouldAddCardDeck(){
            CardDeck createdCardDeck = repository.addCardDeck(1,1);
            assertEquals(1, createdCardDeck.cardId());
            assertEquals(1,createdCardDeck.deckId());
            assertEquals(1,createdCardDeck.quantity());
        }
    }

    @Nested
    class DeleteDeckFromCardDeck{
        @Test
        void shouldDeleteDeck(){
            int validDeckId = 1;
            assertTrue(repository.removeDeck(validDeckId));
        }
        @Test
        void shouldNotDeleteDeck(){
            int validDeckId = Integer.MAX_VALUE;
            assertFalse(repository.removeDeck(validDeckId));
        }
    }
}