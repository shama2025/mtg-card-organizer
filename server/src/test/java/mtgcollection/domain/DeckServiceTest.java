package mtgcollection.domain;

import mtgcollection.TestHelper;
import mtgcollection.data.interfaces.CollectionDeckRepository;
import mtgcollection.data.interfaces.CollectionRepository;
import mtgcollection.data.interfaces.DeckRepository;
import mtgcollection.model.CollectionDeck;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DeckServiceTest {
    @MockBean
    DeckRepository deckRepository;

    @MockBean
    CollectionDeckRepository collectionDeckRepository;

    @MockBean
    CollectionRepository collectionRepository;

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

    @Nested
    class CreateDeckTests{
        @Test
        void shouldCreateDeck(){
            Deck deckToCreate = TestHelper.deckToCreate();
            int validCollectionID = 1;
            Deck createdDeck = TestHelper.deckToCreate();
            createdDeck.setDeckId(3);
            when(collectionRepository.fetchCollectionByCollectionId(validCollectionID)).thenReturn(TestHelper.collection());
            when(deckRepository.createDeck(deckToCreate)).thenReturn(createdDeck);
            when(collectionDeckRepository.createCollectionDeck(createdDeck.getDeckId(),validCollectionID))
                    .thenReturn(new CollectionDeck(3,1));
            Result<Deck> result = deckService.createDeckInCollection(deckToCreate,validCollectionID);
            assertTrue(result.isSuccess());
        }

        @Test
        void shouldNotCreateDeckIfCollectionDoesNotExist(){
            Deck deckToCreate = TestHelper.deckToCreate();
            int invalidCollectionID = Integer.MAX_VALUE;
            when(collectionRepository.fetchCollectionByCollectionId(invalidCollectionID)).thenReturn(null);
            Result<Deck> result = deckService.createDeckInCollection(deckToCreate,invalidCollectionID);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Collection not found."));
        }

        @Test
        void shouldNotCreateDeckWithDateInFuture(){
            Deck deckToCreate = TestHelper.deckToCreate();
            int validCollectionId = 1;
            deckToCreate.setDateCreated(LocalDate.now().plusDays(5));
            Result<Deck> result = deckService.createDeckInCollection(deckToCreate,validCollectionId);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Creation date has to be today."));
        }

        @Test
        void shouldNotCreateDeckWithDateInPast(){
            Deck deckToCreate = TestHelper.deckToCreate();
            int validCollectionId = 1;
            deckToCreate.setDateCreated(LocalDate.now().minusDays(5));
            Result<Deck> result = deckService.createDeckInCollection(deckToCreate,validCollectionId);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Creation date has to be today."));
        }

        @Test
        void shouldNotCreateDeckWithNullName(){
            Deck deckToCreate = TestHelper.deckToCreate();
            int validCollectionId = 1;
            deckToCreate.setName(null);
            Result<Deck> result = deckService.createDeckInCollection(deckToCreate,validCollectionId);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Name cannot be null."));
        }

        @Test
        void shouldNotCreateDeckWithBlankName(){
            Deck deckToCreate = TestHelper.deckToCreate();
            int validCollectionId = 1;
            deckToCreate.setName("");
            Result<Deck> result = deckService.createDeckInCollection(deckToCreate,validCollectionId);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Name cannot be blank."));
        }
    }

    @Nested
    class DeleteDeckTests{
        @Test
        void shouldDeleteDeck(){
            int validDeckId = 1;
            when(deckRepository.fetchDeckByDeckId(validDeckId)).thenReturn(TestHelper.user2Deck());
            when(cardDeckRepository.removeDeck(validDeckId)).thenReturn(true);
            when(collectionDeckRepository.removeDeck(validDeckId)).thenReturn(true);
            when(deckRepository.removeDeck(validDeckId)).thenReturn(true);
            Result<Integer> result = deckService.removeDeck(validDeckId);
            assertTrue(result.isSuccess());
            assertEquals(validDeckId,result.getpayload());
        }

        @Test
        void shouldNotDeleteDeckWhereDeckDoesNotExist(){
            int deckThatDoesNotExistId = Integer.MAX_VALUE;
            when(deckRepository.fetchDeckByDeckId(deckThatDoesNotExistId)).thenReturn(null);
            Result<Integer> result = deckService.removeDeck(deckThatDoesNotExistId);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Deck not found."));
            assertEquals(ResultType.NOT_FOUND,result.getResultType());
        }

        @Test
        void shouldNotDeleteDeckWhenAnotherTransActionFails(){
            // Can Change any transaction to false and will work same
            int validDeckId = 1;
            when(deckRepository.fetchDeckByDeckId(validDeckId)).thenReturn(TestHelper.user2Deck());
            when(cardDeckRepository.removeDeck(validDeckId)).thenReturn(false);
            when(collectionDeckRepository.removeDeck(validDeckId)).thenReturn(true);
            when(deckRepository.removeDeck(validDeckId)).thenReturn(true);
            Result<Integer> result = deckService.removeDeck(validDeckId);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Error deleting deck."));
            assertEquals(ResultType.INVALID,result.getResultType());
        }
    }
}
