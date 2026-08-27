package mtgcollection.domain;

import mtgcollection.TestHelper;
import mtgcollection.data.interfaces.CardDeckRepository;
import mtgcollection.data.interfaces.CardRepository;
import mtgcollection.data.interfaces.DeckRepository;
import mtgcollection.model.CardDeck;
import mtgcollection.model.Deck;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DeckServiceTest {
    @MockBean
    DeckRepository deckRepository;

    @MockBean
    CardRepository cardRepository;

    @MockBean
    CardDeckRepository cardDeckRepository;

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

    @Nested
    class FetchDeckByDeckId{
        @Test
        void shouldFetchDeckByDeckId(){
            int deckThatDoesExist = 2;
            Result<Deck> expected = new Result<>();
            expected.setpayload(TestHelper.user2DeckWithCard());
            when(deckRepository.fetchDeckByDeckId(deckThatDoesExist)).thenReturn(TestHelper.user2DeckWithCard());
            when(cardDeckRepository.fetchAllCardDecksFromDeckId(deckThatDoesExist)).thenReturn(List.of(new CardDeck(1,1,1)));
            when(cardRepository.fetchCardById(1)).thenReturn(TestHelper.lightningBolt());
            Result<Deck> result = deckService.fetchDeckByDeckId(deckThatDoesExist);
            assertTrue(result.isSuccess());
            assertEquals(expected,result);
        }
        @Test
        void shouldNotFetchDeckByDeckIdWhereDeckDoesNotExist(){
            int deckThatDoesNotExist = Integer.MAX_VALUE;
            Result<Deck> expected = new Result<>();
            expected.addErrorMessage("Deck not found.", ResultType.NOT_FOUND);
            when(deckRepository.fetchDeckByDeckId(deckThatDoesNotExist)).thenReturn(null);
            Result<Deck> result = deckService.fetchDeckByDeckId(deckThatDoesNotExist);
            assertFalse(result.isSuccess());
            assertEquals(expected,result);
        }
    }
}
