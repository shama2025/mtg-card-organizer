package mtgcollection.domain;

import mtgcollection.TestHelper;
import mtgcollection.data.interfaces.DeckRepository;
import mtgcollection.model.Deck;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DeckServiceTest {
    @MockBean
    DeckRepository deckRepository;

    @Autowired
    DeckService deckService;

    @Nested
    class FetchAllDecksByCollectionId{
        @Test
        void shouldFetchAllDecksByCollectionId(){
            int validCollectionId = TestHelper.collection().getCollectionId();
            when(deckRepository.fetchAllDecksInACollection(validCollectionId)).thenReturn(List.of(TestHelper.user2Deck()));
            List<Deck> decks = deckService.fetchAllDecksByCollectionId(validCollectionId);
            assertTrue(decks.contains(TestHelper.user2Deck()));
        }
    }
}
