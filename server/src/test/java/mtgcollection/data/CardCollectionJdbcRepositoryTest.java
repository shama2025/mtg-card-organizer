package mtgcollection.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import mtgcollection.TestHelper;
import mtgcollection.data.jdbc.CardCollectionJdbcRepository;
import mtgcollection.data.jdbc.CardJdbcRepository;
import mtgcollection.data.jdbc.CollectionJdbcRepository;
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

}