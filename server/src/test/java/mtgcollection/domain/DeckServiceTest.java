package mtgcollection.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import mtgcollection.TestHelper;
import mtgcollection.data.http.ScryFallApiHttpRepository;
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
import mtgcollection.model.card.Card;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @MockBean
    ScryFallApiHttpRepository scryFallApiHttpRepository;

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
    class UpdateDeck{
        // Should update deck
        @Test
        void shouldUpdateDeck(){
            Deck deckToUpdate = TestHelper.deckToEdit();
            when(deckRepository.fetchDeckByDeckId(deckToUpdate.getDeckId())).thenReturn(deckToUpdate);
            when(deckRepository.updateDeck(deckToUpdate)).thenReturn(true);
            Result<Deck> result = deckService.updateDeck(deckToUpdate);
            assertTrue(result.isSuccess());
        }

        @Test
        void shouldNotUpdateDeckThatHasErrorWhenUpdating(){
            Deck deckToUpdate = TestHelper.deckToEdit();
            when(deckRepository.fetchDeckByDeckId(deckToUpdate.getDeckId())).thenReturn(deckToUpdate);
            when(deckRepository.updateDeck(deckToUpdate)).thenReturn(false);
            Result<Deck> result = deckService.updateDeck(deckToUpdate);
            assertFalse(result.isSuccess());
            assertSame(ResultType.INVALID, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Error updating deck."));
        }

        @Test
        void shouldNotUpdateDeckThatDoesNotExist(){
            Deck deckToUpdate = TestHelper.deckToEdit();
            deckToUpdate.setDeckId(Integer.MAX_VALUE);
            when(deckRepository.fetchDeckByDeckId(deckToUpdate.getDeckId())).thenThrow(EmptyResultDataAccessException.class);
            Result<Deck> result = deckService.updateDeck(deckToUpdate);
            assertFalse(result.isSuccess());
            assertSame(ResultType.NOT_FOUND, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Deck does not exist."));
        }


        @Test
        void shouldNotUpdateDeckWithBlankName(){
            Deck deckToUpdate = TestHelper.deckToEdit();
            deckToUpdate.setName("");
            Result<Deck> result = deckService.updateDeck(deckToUpdate);
            assertFalse(result.isSuccess());
            assertSame(ResultType.INVALID, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Name cannot be blank."));
        }
        @Test
        void shouldNotUpdateDeckWithNullName(){
            Deck deckToUpdate = TestHelper.deckToEdit();
            deckToUpdate.setName(null);
            Result<Deck> result = deckService.updateDeck(deckToUpdate);
            assertFalse(result.isSuccess());
            assertSame(ResultType.INVALID, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Name cannot be null."));
        }
    }

    @Nested
    class AddCardToDeck{
        @Test
        void shouldAddCardToDeck() throws InterruptedException, JsonProcessingException {
            Deck decKToUpdate = TestHelper.deckToEdit();
            String cardDoesExist = "black lotus";
            Card blackLotus = TestHelper.blackLotus();
            CardDeck cardDeck = new CardDeck(blackLotus.getId(),decKToUpdate.getDeckId(),1);
            blackLotus.setId(5);
            when(deckRepository.fetchDeckByDeckId(decKToUpdate.getDeckId())).thenReturn(decKToUpdate);
            when(cardRepository.fetchCardByName(cardDoesExist.toUpperCase())).thenReturn(blackLotus);
            when(cardDeckRepository.addCardDeck(blackLotus.getId(), decKToUpdate.getDeckId()))
                    .thenReturn(cardDeck);
            when(deckRepository.updateDeck(decKToUpdate)).thenReturn(true);
            Result<Deck> result = deckService.addCardToDeck(decKToUpdate, cardDoesExist);
            assertTrue(result.isSuccess());
        }

        @Test
        void shouldNotAddCardToDeckWhenThereIsAnError() throws InterruptedException, JsonProcessingException {
            Deck decKToUpdate = TestHelper.deckToEdit();
            String cardDoesExist = "black lotus";
            Card blackLotus = TestHelper.blackLotus();
            CardDeck cardDeck = new CardDeck(blackLotus.getId(),decKToUpdate.getDeckId(),1);
            blackLotus.setId(5);
            when(deckRepository.fetchDeckByDeckId(decKToUpdate.getDeckId())).thenReturn(decKToUpdate);
            when(cardRepository.fetchCardByName(cardDoesExist.toUpperCase())).thenReturn(blackLotus);
            when(cardDeckRepository.addCardDeck(blackLotus.getId(), decKToUpdate.getDeckId()))
                    .thenReturn(cardDeck);
            when(deckRepository.updateDeck(decKToUpdate)).thenReturn(false);
            Result<Deck> result = deckService.addCardToDeck(decKToUpdate, cardDoesExist);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Error updating deck."));
            assertEquals(ResultType.INVALID, result.getResultType());
        }

        @Test
        void shouldNotAddCardToDeckWhereCardDeckIsNull() throws InterruptedException, JsonProcessingException {
            Deck decKToUpdate = TestHelper.deckToEdit();
            String cardDoesExist = "black lotus";
            Card blackLotus = TestHelper.blackLotus();
            blackLotus.setId(5);
            when(deckRepository.fetchDeckByDeckId(decKToUpdate.getDeckId())).thenReturn(decKToUpdate);
            when(cardRepository.fetchCardByName(cardDoesExist.toUpperCase())).thenReturn(blackLotus);
            when(cardDeckRepository.addCardDeck(blackLotus.getId(), decKToUpdate.getDeckId()))
                    .thenReturn(null);
            Result<Deck> result = deckService.addCardToDeck(decKToUpdate, cardDoesExist);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Error adding card to deck."));
            assertEquals(ResultType.INVALID, result.getResultType());
        }

        @Test
        void shouldNotAddCardToDeckWhereCardDoesNotExist() throws InterruptedException, JsonProcessingException {
            Deck decKToUpdate = TestHelper.deckToEdit();
            String cardDoesNotExist = "blahblah";
            when(deckRepository.fetchDeckByDeckId(decKToUpdate.getDeckId())).thenReturn(decKToUpdate);
            when(cardRepository.fetchCardByName(cardDoesNotExist)).thenThrow(EmptyResultDataAccessException.class);
            when(scryFallApiHttpRepository.fetchCardFromScryfallByName(cardDoesNotExist)).thenReturn(Optional.empty());
            Result<Deck> result = deckService.addCardToDeck(decKToUpdate,cardDoesNotExist);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Card not found."));
            assertEquals(ResultType.NOT_FOUND, result.getResultType());
        }

        @Test
        void shouldNotAddCardToDeckThatDoesNotExist() throws InterruptedException, JsonProcessingException {
            Deck deckToUpdate = TestHelper.deckToEdit();
            deckToUpdate.setDeckId(Integer.MAX_VALUE);
            when(deckRepository.fetchDeckByDeckId(deckToUpdate.getDeckId())).thenThrow(EmptyResultDataAccessException.class);
            Result<Deck> result = deckService.addCardToDeck(deckToUpdate,"");
            assertFalse(result.isSuccess());
            assertSame(ResultType.NOT_FOUND, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Deck does not exist."));
        }

        @Test
        void shouldNotAddCardToDeckDeckWithBlankName() throws InterruptedException, JsonProcessingException {
            Deck deckToUpdate = TestHelper.deckToEdit();
            deckToUpdate.setName("");
            Result<Deck> result = deckService.addCardToDeck(deckToUpdate,"");
            assertFalse(result.isSuccess());
            assertSame(ResultType.INVALID, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Name cannot be blank."));
        }
        @Test
        void shouldNotAddCardToDeckDeckWithNullName() throws InterruptedException, JsonProcessingException {
            Deck deckToUpdate = TestHelper.deckToEdit();
            deckToUpdate.setName(null);
            Result<Deck> result = deckService.addCardToDeck(deckToUpdate,"");
            assertFalse(result.isSuccess());
            assertSame(ResultType.INVALID, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Name cannot be null."));
        }
    }

    @Nested
    class UpdateCardInADeckTest{
        @Test
        void shouldUpdateCardInDeck(){
            int validCardId = 1;
            int validDeckId = 1;
            int validQuantity = 10;
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setDeckId(1);
            deckToEdit.setDateUpdated(LocalDate.now());
            when(cardRepository.fetchCardById(validCardId)).thenReturn(TestHelper.blackLotus());
            when(deckRepository.fetchDeckByDeckId(validDeckId)).thenReturn(deckToEdit);
            when(cardDeckRepository.updateCardInDeck(validCardId,validDeckId,validQuantity)).thenReturn(true);
            when(deckRepository.updateDeck(deckToEdit)).thenReturn(true);
            Result<Integer> result = deckService.updateCardInADeck(validCardId,validDeckId,validQuantity);
            assertTrue(result.isSuccess());
            assertEquals(validDeckId,result.getpayload());
        }
        @Test
        void shouldNotUpdateDeckWhereUpdatingCardFails(){
            int validCardId = 1;
            int validDeckId = 1;
            int validQuantity = 10;
            when(cardRepository.fetchCardById(validCardId)).thenReturn(TestHelper.blackLotus());
            when(deckRepository.fetchDeckByDeckId(validDeckId)).thenReturn(TestHelper.user2Deck());
            when(cardDeckRepository.updateCardInDeck(validCardId,validDeckId,validQuantity)).thenReturn(false);
            Result<Integer> result = deckService.updateCardInADeck(validCardId,validDeckId,validQuantity);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.INVALID, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Error updating card in a deck."));
        }
        @Test
        void shouldNotUpdateDeckWhereDeckDoesNotExist(){
            int validCardId = 1;
            int inValidDeckId = Integer.MAX_VALUE;
            int validQuantity = 10;
            when(cardRepository.fetchCardById(validCardId)).thenReturn(TestHelper.blackLotus());
            when(deckRepository.fetchDeckByDeckId(inValidDeckId)).thenThrow(EmptyResultDataAccessException.class);
            Result<Integer> result = deckService.updateCardInADeck(validCardId,inValidDeckId,validQuantity);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.NOT_FOUND, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Deck does not exist."));
        }
        @Test
        void shouldNotUpdateDeckWhereCardDoesNotExist(){
            int inValidCardId = 1;
            int validDeckId = 1;
            int validQuantity = 10;
            when(cardRepository.fetchCardById(inValidCardId)).thenThrow(EmptyResultDataAccessException.class);
            Result<Integer> result = deckService.updateCardInADeck(inValidCardId,validDeckId,validQuantity);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.NOT_FOUND, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Card does not exist."));
        }
        @Test
        void shouldNotUpdateCardInADeckWhereQuantityIsBelow0(){
            int validCardId = 1;
            int validDeckId = 1;
            int invalidQuantity = -10;
            Result<Integer> result = deckService.updateCardInADeck(validCardId,validDeckId,invalidQuantity);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.INVALID, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Quantity cannot be below 0."));
        }
    }

    @Nested
    class RemoveCardFromDeckTest{
        @Test
        void shouldRemoveCardFromDeckCardInDeck(){
            int validCardId = 1;
            int validDeckId = 1;
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setDeckId(1);
            deckToEdit.setDateUpdated(LocalDate.now());
            when(cardRepository.fetchCardById(validCardId)).thenReturn(TestHelper.blackLotus());
            when(deckRepository.fetchDeckByDeckId(validDeckId)).thenReturn(deckToEdit);
            when(cardDeckRepository.removeCardFromDeck(validCardId,validDeckId)).thenReturn(true);
            when(deckRepository.updateDeck(deckToEdit)).thenReturn(true);
            Result<Integer> result = deckService.removeCardFromDeck(validCardId,validDeckId);
            assertTrue(result.isSuccess());
            assertEquals(validDeckId,result.getpayload());
        }
        @Test
        void shouldNotRemoveCardFromDeckWhereUpdatingCardFails(){
            int validCardId = 1;
            int validDeckId = 1;
            when(cardRepository.fetchCardById(validCardId)).thenReturn(TestHelper.blackLotus());
            when(deckRepository.fetchDeckByDeckId(validDeckId)).thenReturn(TestHelper.user2Deck());
            when(cardDeckRepository.removeCardFromDeck(validCardId,validDeckId)).thenReturn(false);
            Result<Integer> result = deckService.removeCardFromDeck(validCardId,validDeckId);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.INVALID, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Error updating card in a deck."));
        }
        @Test
        void shouldNotRemoveCardFromDeckWhereDeckDoesNotExist(){
            int validCardId = 1;
            int inValidDeckId = Integer.MAX_VALUE;
            when(cardRepository.fetchCardById(validCardId)).thenReturn(TestHelper.blackLotus());
            when(deckRepository.fetchDeckByDeckId(inValidDeckId)).thenThrow(EmptyResultDataAccessException.class);
            Result<Integer> result = deckService.removeCardFromDeck(validCardId,inValidDeckId);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.NOT_FOUND, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Deck does not exist."));
        }
        @Test
        void shouldNotRemoveCardFromDeckWhereCardDoesNotExist(){
            int inValidCardId = 1;
            int validDeckId = 1;
            when(cardRepository.fetchCardById(inValidCardId)).thenThrow(EmptyResultDataAccessException.class);
            Result<Integer> result = deckService.removeCardFromDeck(inValidCardId,validDeckId);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.NOT_FOUND, result.getResultType());
            assertTrue(result.getErrorMessages().contains("Card does not exist."));
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
