package mtgcollection.data.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import mtgcollection.TestHelper;
import mtgcollection.model.card.Card;
import mtgcollection.model.CardCollection;
import mtgcollection.model.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CardCollectionJdbcRepositoryTest {

    @Autowired
    JdbcClient jdbcClient;

    @Autowired
    CardCollectionJdbcRepository cardCollectionJdbcRepository;

    @Autowired
    CollectionJdbcRepository collectionJdbcRepository;

    @Autowired
    CardJdbcRepository cardJdbcRepository;

    @BeforeEach
    void runSetKnownGoodState(){
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Nested
    class AddCardToCardCollectionTest{
        @Test
        void shouldAddCardToCollection() throws JsonProcessingException {
            Card card = cardJdbcRepository.addCard(TestHelper.lightningBolt());
            Collection collection = new Collection(1,1);
            CardCollection cardCollection = cardCollectionJdbcRepository.addCardToCollection(card,collection);
            assertEquals(card,cardCollection.card());
            assertEquals(collection,cardCollection.collection());
        }
    }

    @Nested
    class DeleteCardFromCollection{
        // Should delete
        @Test
        void shouldDeleteCard(){
            int cardThatExistId = 1;
            int collectionDoesExist = 1;
            assertTrue(cardCollectionJdbcRepository.removeCardFromCollection(cardThatExistId,collectionDoesExist));
        }

        // Should not delete when collection does not exist
        @Test
        void shouldNotDeleteWhenCollectionDoesNotExist(){
            int cardThatExistId = 1;
            int collectionDoesNotExist = Integer.MAX_VALUE;
            assertFalse(cardCollectionJdbcRepository.removeCardFromCollection(cardThatExistId,collectionDoesNotExist));
        }

        // Should not delete when card does not exist
        @Test
        void shouldNotDeleteWhenCardDoesNotExist(){
            int collectionDoesExist = 1;
            int cardThatDoesNotExistId = Integer.MAX_VALUE;
            assertFalse(cardCollectionJdbcRepository.removeCardFromCollection(cardThatDoesNotExistId,collectionDoesExist));
        }
    }

    @Nested
    class UpdateCardFromCollection{
        // Should Update
        @Test
        void shouldUpdateCardQuantity(){
            int cardQuantity = 5;
            int collectionThatDoesExists = TestHelper.collection().getCollectionId();
            Card cardThatDoesExists = TestHelper.lightningBolt();
            cardThatDoesExists.setId(1);
            boolean isUpdated = cardCollectionJdbcRepository.updateCardInCollection(cardThatDoesExists.getId(),collectionThatDoesExists,cardQuantity);
            assertTrue(isUpdated);
        }

        // Should not update where card does not exist
        @Test
        void shouldNotUpdateCardWhereCardDoesNotExist(){
            int cardQuantity = 5;
            int collectionThatDoesExists = TestHelper.collection().getCollectionId();
            int cardThatDoesNotExists = Integer.MAX_VALUE;
            boolean isUpdated = cardCollectionJdbcRepository.updateCardInCollection(cardThatDoesNotExists,collectionThatDoesExists,cardQuantity);
            assertFalse(isUpdated);
        }
        // Should not update where collection does not exist
        @Test
        void shouldNotUpdateCardWhereCollectionDoesNotExist(){
            int cardQuantity = 5;
            int collectionThatDoesNotExists = Integer.MAX_VALUE;
            int cardThatExists = TestHelper.lightningBolt().getId();
            boolean isUpdated = cardCollectionJdbcRepository.updateCardInCollection(cardThatExists,collectionThatDoesNotExists,cardQuantity);
            assertFalse(isUpdated);
        }

        // Should not edit card below 0
        @Test
        void shouldNotUpdateCardQuantityBelow0(){
            int cardQuantity = -5;
            int collectionThatExists = TestHelper.collection().getCollectionId();
            int cardThatExists = TestHelper.lightningBolt().getId();
            boolean isUpdated = cardCollectionJdbcRepository.updateCardInCollection(cardThatExists,collectionThatExists,cardQuantity);
            assertFalse(isUpdated);
        }
    }

}