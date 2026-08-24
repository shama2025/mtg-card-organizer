package mtgcollection.domain;

import mtgcollection.TestHelper;
import mtgcollection.data.CardRepository;
import mtgcollection.model.Card;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CollectionServiceTest {

    @MockBean
    CardRepository cardRepository;

    @Autowired
    CollectionService collectionService;

    @Nested
    class FetchAllCardsByUserTest{
        @Test
        void shouldFetchAllUsers(){
            when(cardRepository.fetchAllCards(1)).thenReturn(TestHelper.cardList());
            List<Card> cardList = collectionService.fetchAllCardsByUserId(1);
            assertEquals(3,cardList.size());
        }
    }

}