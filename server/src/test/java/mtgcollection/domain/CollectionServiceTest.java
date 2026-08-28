package mtgcollection.domain;

import mtgcollection.TestHelper;
import mtgcollection.data.http.ScryFallApiHttpRepository;
import mtgcollection.data.interfaces.CardCollectionRepository;
import mtgcollection.data.interfaces.CardRepository;
import mtgcollection.data.interfaces.CollectionRepository;
import mtgcollection.model.CardCollection;
import mtgcollection.model.card.Card;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CollectionServiceTest {

    @MockBean
    CardRepository cardRepository;

    @MockBean
    ScryFallApiHttpRepository scryFallApiHttpRepository;

    @MockBean
    CardCollectionRepository cardCollectionRepository;

    @MockBean
    CollectionRepository collectionRepository;

    @Autowired
    CollectionService collectionService;

    @Nested
    class FetchAllCardsByUserTest{
        @Test
        void shouldFetchAllUsers(){
            when(cardRepository.fetchAllCards(1)).thenReturn(TestHelper.cardList());
            List<Card> cardList = collectionService.fetchAllCardsByCollection(1);
            assertEquals(3,cardList.size());
        }
    }

    @Nested
    class AddCardToCollection{
        // Should Add card that is not present in card table
        @Test
        void shouldAddCardThatDoesNotExistInCardTable() throws Exception {
            Card cardNotInCardTable = TestHelper.blackLotus();
            Card expected = TestHelper.blackLotus();
            int validCollectionId = TestHelper.collection().getCollectionId();
            expected.setId(5);

            when(collectionRepository.fetchCollectionByCollectionId(validCollectionId)).thenReturn(TestHelper.collection());
            when(cardRepository.fetchCardByName(cardNotInCardTable.getName().toUpperCase()))
                    .thenThrow(EmptyResultDataAccessException.class);
            when(scryFallApiHttpRepository.fetchCardFromScryfallByName(cardNotInCardTable.getName()))
                    .thenReturn(Optional.of(TestHelper.blackLotusCardResponse()));
            when(cardRepository.addCard(any(Card.class)))
                    .thenReturn(expected);
            when(cardCollectionRepository.addCardToCollection(expected.getId(), validCollectionId))
                    .thenReturn(new CardCollection(expected.getId(), validCollectionId, 1));

            Result<Card> result = collectionService.addCardToCollection(cardNotInCardTable.getName(), validCollectionId);

            assertTrue(result.isSuccess());
            assertEquals(expected, result.getpayload());
            assertEquals(5, result.getpayload().getId());
        }

        @Test
        void shouldAddCardThatExistsInCardTable() throws Exception {
            Card cardPresentInCardTable = TestHelper.solRing();
            int validCollectionId = TestHelper.collection().getCollectionId();

            when(collectionRepository.fetchCollectionByCollectionId(validCollectionId)).thenReturn(TestHelper.collection());
            when(cardRepository.fetchCardByName(cardPresentInCardTable.getName().toUpperCase()))
                    .thenReturn(cardPresentInCardTable);
            when(cardCollectionRepository.addCardToCollection(cardPresentInCardTable.getId(), validCollectionId))
                    .thenReturn(new CardCollection(cardPresentInCardTable.getId(), validCollectionId, 1));

            Result<Card> result = collectionService.addCardToCollection(cardPresentInCardTable.getName(), validCollectionId);

            assertTrue(result.isSuccess());
            assertEquals(cardPresentInCardTable, result.getpayload());
        }

        // Should not add card if name card does not exist
        @Test
        void shouldNotAddCardThatDoesNotExist() throws Exception {
            String notARealCard = "asdfhwhifjjalwl";
            int validCollectionId = TestHelper.collection().getCollectionId();
            when(collectionRepository.fetchCollectionByCollectionId(1)).thenReturn(TestHelper.collection());
            Result<Card> result = collectionService.addCardToCollection(notARealCard,validCollectionId);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Card not found."));
            assertSame(ResultType.NOT_FOUND,result.getResultType());
        }

        // Should not add card if collection does not exist
        @Test
        void shouldNotCardIfCollectionIsNotFound() throws Exception {
            int doesNotExist = TestHelper.collection().getCollectionId();
            Result<Card> result = collectionService.addCardToCollection("Black Lotus",doesNotExist);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Collection was not found."));
            assertSame(ResultType.NOT_FOUND,result.getResultType());
        }

        // Should not add card if name is blank/null
        @Test
        void shouldNotAddCardIfNameIsNull() throws Exception {
            int validCollectionid = TestHelper.collection().getCollectionId();
            when(collectionRepository.fetchCollectionByCollectionId(1)).thenReturn(TestHelper.collection());
            Result<Card> result = collectionService.addCardToCollection(null,validCollectionid);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Card name cannot be empty."));
            assertSame(ResultType.INVALID,result.getResultType());
        }
        @Test
        void shouldNotAddCardIfNameIsBlank() throws Exception {
            int validCollection = TestHelper.collection().getCollectionId();
            when(collectionRepository.fetchCollectionByCollectionId(1)).thenReturn(TestHelper.collection());
            Result<Card> result = collectionService.addCardToCollection("", validCollection);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Card name cannot be empty."));
            assertSame(ResultType.INVALID,result.getResultType());
        }
    }

    @Nested
    class DeleteCardFromCollectionTest{
        @Test
        void shouldDeleteCardFromCollection(){
            int cardThatExists = TestHelper.lightningBolt().getId();
            int collectionThatExists = TestHelper.collection().getCollectionId();
            when(cardRepository.fetchCardById(cardThatExists)).thenReturn(TestHelper.lightningBolt());
            when(collectionRepository.fetchCollectionByCollectionId(collectionThatExists)).thenReturn(TestHelper.collection());
            when(cardCollectionRepository.removeCardFromCollection(cardThatExists,collectionThatExists)).thenReturn(true);
            Result<Integer> result = collectionService.removeCardFromCollection(cardThatExists,collectionThatExists);
            assertTrue(result.isSuccess());
        }

        @Test
        void shouldNotDeleteCardFromCollectionWhenCardDoesNotExist(){
            int cardThatExists = TestHelper.lightningBolt().getId();
            int collectionThatDoesNotExist = Integer.MAX_VALUE;
            Result<Integer> expected = new Result<>();
            expected.addErrorMessage("Collection was not found.",ResultType.NOT_FOUND);
            when(cardRepository.fetchCardById(cardThatExists)).thenReturn(TestHelper.lightningBolt());
            when(collectionRepository.fetchCollectionByCollectionId(collectionThatDoesNotExist)).thenReturn(null);
            Result<Integer> result = collectionService.removeCardFromCollection(cardThatExists,collectionThatDoesNotExist);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.NOT_FOUND,result.getResultType());
            assertEquals(expected,result);
        }

        @Test
        void shouldNotDeleteCardFromCollectionWhenCollectionDoesNotExist(){
            int cardThatDoesNotExists = Integer.MAX_VALUE;
            int collectionThatExist = TestHelper.collection().getCollectionId();
            Result<Integer> expected = new Result<>();
            expected.addErrorMessage("Card was not found.",ResultType.NOT_FOUND);
            when(collectionRepository.fetchCollectionByCollectionId(collectionThatExist)).thenReturn(TestHelper.collection());
            Result<Integer> result = collectionService.removeCardFromCollection(cardThatDoesNotExists,collectionThatExist);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.NOT_FOUND,result.getResultType());
            assertEquals(expected,result);
        }
    }

    @Nested
    class UpdateCardFromCollectionTest{
        // Should update card quantity
        @Test
        void shouldUpdateCard(){
            int validQuantity = 4;
            Card cardThatExists = TestHelper.lightningBolt();
            cardThatExists.setId(1);
            int collectionThatDoesExist = TestHelper.collection().getCollectionId();
            Result<Integer> expected = new Result<>();
            expected.setpayload(cardThatExists.getId());
            when(cardRepository.fetchCardById(cardThatExists.getId())).thenReturn(cardThatExists);
            when(collectionRepository.fetchCollectionByCollectionId(collectionThatDoesExist)).thenReturn(TestHelper.collection());
            when(cardCollectionRepository.updateCardInCollection(cardThatExists.getId(),collectionThatDoesExist,validQuantity)).thenReturn(true);
            Result<Integer> result = collectionService.updateCardInCollection(cardThatExists.getId(),collectionThatDoesExist,validQuantity);
            assertTrue(result.isSuccess());
            assertEquals(expected,result);
        }

        // Should not update quantity if less than 0
        @Test
        void shouldNotUpdateCardWhenQuantityIsBelow0(){
            int invalidQuantity = -4;
            Card cardThatExists = TestHelper.lightningBolt();
            cardThatExists.setId(1);
            int collectionThatDoesExist = TestHelper.collection().getCollectionId();
            Result<Integer> expected = new Result<>();
            expected.addErrorMessage("Quantity cannot be below 0.", ResultType.INVALID);
            when(cardRepository.fetchCardById(cardThatExists.getId())).thenReturn(cardThatExists);
            when(collectionRepository.fetchCollectionByCollectionId(collectionThatDoesExist)).thenReturn(TestHelper.collection());
            Result<Integer> result = collectionService.updateCardInCollection(cardThatExists.getId(),collectionThatDoesExist,invalidQuantity);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.INVALID,result.getResultType());
            assertEquals(expected,result);
        }

        // should not update quantity if collection is not found
        @Test
        void shouldNotUpdateCardWhenCollectionDoesNotExist(){
            int validQuantity = 4;
            Card cardThatExists = TestHelper.lightningBolt();
            cardThatExists.setId(1);
            int collectionThatDoesNotExist = Integer.MAX_VALUE;
            Result<Integer> expected = new Result<>();
            expected.addErrorMessage("Collection was not found.",ResultType.NOT_FOUND);
            when(cardRepository.fetchCardById(cardThatExists.getId())).thenReturn(cardThatExists);
            when(collectionRepository.fetchCollectionByCollectionId(collectionThatDoesNotExist)).thenReturn(null);
            Result<Integer> result = collectionService.updateCardInCollection(cardThatExists.getId(),collectionThatDoesNotExist,validQuantity);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.NOT_FOUND,result.getResultType());
            assertEquals(expected,result);
        }

        // Should not update quanityt if card is not found
        @Test
        void shouldNotUpdateCardWhenCardDoesNotExist(){
            int validQuantity = 4;
            int cardThatDoesNotExists = Integer.MAX_VALUE;
            int collectionThatExist = TestHelper.collection().getCollectionId();
            Result<Integer> expected = new Result<>();
            expected.addErrorMessage("Card was not found.",ResultType.NOT_FOUND);
            when(collectionRepository.fetchCollectionByCollectionId(collectionThatExist)).thenReturn(TestHelper.collection());
            Result<Integer> result = collectionService.updateCardInCollection(cardThatDoesNotExists,collectionThatExist,validQuantity);
            assertFalse(result.isSuccess());
            assertEquals(ResultType.NOT_FOUND,result.getResultType());
            assertEquals(expected,result);
        }
    }
}