package mtgcollection.data.jdbc;

import mtgcollection.TestHelper;
import mtgcollection.data.interfaces.CollectionDeckRepository;
import mtgcollection.model.CollectionDeck;
import mtgcollection.model.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CollectionDeckJdbcRepositoryTest {

    @Autowired
    JdbcClient jdbcClient;

    @Autowired
    CollectionDeckRepository repository;

    @Autowired
    DeckJdbcRepository deckJdbcRepository;

    @BeforeEach
    void runSetKnownGoodState(){
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Nested
    class CreateCollectionDeckTest{
        @Test
        void shouldCreateCollectionDeck(){
            Deck deckCreated = deckJdbcRepository.createDeck(TestHelper.deckToCreate());
            CollectionDeck collectionDeck = repository.createCollectionDeck(deckCreated.getDeckId(),1);
            assertEquals(3,collectionDeck.deckId());
            assertEquals(1,collectionDeck.collectionId());
        }
    }

    @Nested
    class DeleteDeckTest{
        @Test
        void shouldDeleteDeck(){
            int validDeck = 1;
            assertTrue(repository.removeDeck(validDeck));
        }
        @Test
        void shouldNotDeleteDeck(){
            int invalidDeck = Integer.MAX_VALUE;
            assertFalse(repository.removeDeck(invalidDeck));
        }
    }
}