package mtgcollection.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import mtgcollection.TestHelper;
import mtgcollection.data.jdbc.CardJdbcRepository;
import mtgcollection.model.card.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CardJdbcRepositoryTest {

    @Autowired
    JdbcClient jdbcClient;

    @Autowired
    CardJdbcRepository repository;

    @BeforeEach
    void runSetKnownGoodState(){
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Nested
    class FetchAllCardsInUserCollectionTest{
        @Test
        void shouldFetchAllCardsForAGivenUser(){
            List<Card> cardList = repository.fetchAllCards(1);
            assertEquals(3, cardList.size());
        }
    }

    @Nested
    class AddCardTests{
        // Should Add Card
        @Test
        void shouldAddCardWithPopulatedValues() throws JsonProcessingException {
            Card cardToAdd = TestHelper.lightningBolt();
            Card createdCard = repository.addCard(cardToAdd);
            createdCard.setId(5);
            assertEquals(cardToAdd,createdCard);
        }

        // Should add card with empty manaColor
        @Test
        void shouldAddCardWithEmptyManaColor() throws JsonProcessingException{
            Card cardToAdd = TestHelper.lightningBolt();
            cardToAdd.setManaColor(null);
            Card createdCard = repository.addCard(cardToAdd);
            createdCard.setId(5);
            assertEquals(cardToAdd,createdCard);
        }

        // Should not add null artist name
        @Test
        void shouldNotAddCardWithNullArtist(){
            Card cardToAdd = TestHelper.lightningBolt();
            cardToAdd.setArtistName(null);
            assertThrows(DataIntegrityViolationException.class,() ->{
                repository.addCard(cardToAdd);
            });

        }

        // Should not add card with null legalities
        @Test
        void shouldNotAddCardWithNullLegalities(){
            Card cardToAdd = TestHelper.lightningBolt();
            cardToAdd.setLegalities(null);
            assertThrows(NullPointerException.class,() ->{
                repository.addCard(cardToAdd);
            });

        }

        // Should not add card with null set
        @Test
        void shouldNotAddCardWithNullSet(){
            Card cardToAdd = TestHelper.lightningBolt();
            cardToAdd.setSet(null);
            assertThrows(NullPointerException.class,() ->{
                repository.addCard(cardToAdd);
            });

        }


        // Should not add card with null image path
        @Test
        void shouldNotAddCardWithNullImagePath(){
            Card cardToAdd = TestHelper.lightningBolt();
            cardToAdd.setImgPath(null);
            assertThrows(DataIntegrityViolationException.class,() ->{
                repository.addCard(cardToAdd);
            });

        }

        // Should not add card if name is null
        @Test
        void shouldNotAddCardWithNullName(){
            Card cardToAdd = TestHelper.lightningBolt();
            cardToAdd.setName(null);
            assertThrows(DataIntegrityViolationException.class,() ->{
                repository.addCard(cardToAdd);
            });

        }

        // Should Not add card if uuid is null
        @Test
        void shouldNotAddCardWithNullUUID(){
            Card cardToAdd = TestHelper.lightningBolt();
            cardToAdd.setCardId(null);
            assertThrows(NullPointerException.class,() ->{
                repository.addCard(cardToAdd);
            });

        }

        // Should Not add Card with null ManaCost
        @Test
        void shouldNotAddCardWithNullManaCost(){
            Card cardToAdd = TestHelper.lightningBolt();
            cardToAdd.setManaCost(null);
            assertThrows(NullPointerException.class,() ->{
                repository.addCard(cardToAdd);
            });

        }

    }

}