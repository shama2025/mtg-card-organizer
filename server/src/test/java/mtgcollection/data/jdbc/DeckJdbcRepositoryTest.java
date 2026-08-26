package mtgcollection.data.jdbc;

import mtgcollection.TestHelper;
import mtgcollection.model.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DeckJdbcRepositoryTest {

    @Autowired
    JdbcClient jdbcClient;

    @Autowired
    DeckJdbcRepository deckJdbcRepository;

    @BeforeEach
    void runSetKnownGoodState(){
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Nested
    class FetchAllDecksForUser{
        @Test
        void shouldFetchAllDecks(){
            List<Deck> decks = deckJdbcRepository.fetchAllDecksInACollection(2);
            assertTrue(decks.contains(TestHelper.user2Deck()));
        }
    }

    @Nested
    class FetchOneDeckByDeckId{
        @Test
        void shouldFetchOneDeckByID(){
            int deckId = 2;
            Deck deck = deckJdbcRepository.fetchDeckByDeckId(deckId);
            assertEquals(TestHelper.user2Deck(),deck);
        }
    }

    @Nested
    class CreateDeckTest{
        @Test
        void shouldCreateDeck(){
            Deck deckToCreate = TestHelper.deckToCreate();
            Deck createdDeck = deckJdbcRepository.createDeck(deckToCreate);
            deckToCreate.setDeckId(3);
            deckToCreate.setDateCreated(LocalDate.now());
            assertEquals(deckToCreate,createdDeck);
        }
        @Test
        void shouldNotCreatDeckWithNullName(){
            Deck deckToCreate = TestHelper.deckToCreate();
            deckToCreate.setName(null);
            assertThrows(DataIntegrityViolationException.class,()->{
                deckJdbcRepository.createDeck(deckToCreate);
            });
        }
        @Test
        void shouldNotCreateDeckWithNullCreatedDate(){
            Deck deckToCreate = TestHelper.deckToCreate();
            deckToCreate.setDateCreated(null);
            assertThrows(DataIntegrityViolationException.class,()->{
                deckJdbcRepository.createDeck(deckToCreate);
            });
        }
    }

}