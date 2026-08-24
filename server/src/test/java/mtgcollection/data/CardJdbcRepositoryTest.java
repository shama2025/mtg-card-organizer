package mtgcollection.data;

import mtgcollection.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

}