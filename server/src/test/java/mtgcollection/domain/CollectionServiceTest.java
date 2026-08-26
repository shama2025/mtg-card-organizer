package mtgcollection.domain;

import mtgcollection.TestHelper;
import mtgcollection.data.http.ScryFallApiHttpRepository;
import mtgcollection.data.http.response.model.CardResponse;
import mtgcollection.data.interfaces.CardCollectionRepository;
import mtgcollection.data.interfaces.CardRepository;
import mtgcollection.data.interfaces.CollectionRepository;
import mtgcollection.model.CardCollection;
import mtgcollection.model.card.Card;
import mtgcollection.model.Collection;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
            Card cardNotInCardTable =TestHelper.blackLotus();
            Card expected = TestHelper.blackLotus();
            expected.setId(5);
            when(collectionRepository.fetchCollectionByCollectionId(TestHelper.collection().getCollectionId()))
                    .thenReturn(TestHelper.collection());
            when(cardRepository.fetchCardByName(cardNotInCardTable.getName()))
                    .thenReturn(null);
            when(scryFallApiHttpRepository.fetchCardFromScryfallByName(cardNotInCardTable.getName()))
                    .thenReturn(Optional.of(TestHelper.blackLotusCardResponse()));
            when(cardRepository.addCard(any(Card.class)))
                    .thenReturn(expected);
            when(cardCollectionRepository.addCardToCollection(eq(expected), any(Collection.class)))
                    .thenReturn(new CardCollection(expected, TestHelper.collection(), 1));
            Result<Card> result = collectionService.addCardToCollection(cardNotInCardTable.getName(),TestHelper.collection());
            assertTrue(result.isSuccess());
            assertEquals(expected, result.getpayload());
        }

        // Should Add Card that is present in card table
        @Test
        void shouldAddCardThatExistsInCardTable() throws Exception {
            Card cardPresentInCardTable = TestHelper.solRing();
            Card expected = TestHelper.solRing();
            expected.setId(5);
            when(collectionRepository.fetchCollectionByCollectionId(TestHelper.collection().getCollectionId()))
                    .thenReturn(TestHelper.collection());
            when(cardRepository.fetchCardByName(eq(cardPresentInCardTable.getName().toUpperCase())))
                    .thenReturn(cardPresentInCardTable);
            when(cardRepository.addCard(any(Card.class)))
                    .thenReturn(expected);
            when(cardCollectionRepository.addCardToCollection(eq(expected), any(Collection.class)))
                    .thenReturn(new CardCollection(expected, TestHelper.collection(), 1));
            Result<Card> result = collectionService.addCardToCollection(cardPresentInCardTable.getName(),TestHelper.collection());
            assertTrue(result.isSuccess());
            assertEquals(expected, result.getpayload());
        }

        // Should not add card if name card does not exist
        @Test
        void shouldNotAddCardThatDoesNotExist() throws Exception {
            when(collectionRepository.fetchCollectionByCollectionId(1)).thenReturn(TestHelper.collection());
            String notARealCard = "asdfhwhifjjalwl";
            Result<Card> result = collectionService.addCardToCollection(notARealCard,TestHelper.collection());
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Card not found."));
            assertSame(ResultType.NOT_FOUND,result.getResultType());
        }

        // Should not add card if collection does not exist
        @Test
        void shouldNotCardIfCollectionIsNotFound() throws Exception {
            Collection doesNotExist = TestHelper.collection();
            doesNotExist.setCollectionId(Integer.MAX_VALUE);
            Result<Card> result = collectionService.addCardToCollection("Black Lotus",doesNotExist);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Collection was not found."));
            assertSame(ResultType.NOT_FOUND,result.getResultType());
        }

        // Should not add card if name is blank/null
        @Test
        void shouldNotAddCardIfNameIsNull() throws Exception {
            when(collectionRepository.fetchCollectionByCollectionId(1)).thenReturn(TestHelper.collection());
            Result<Card> result = collectionService.addCardToCollection(null,TestHelper.collection());
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessages().contains("Card name cannot be empty."));
            assertSame(ResultType.INVALID,result.getResultType());
        }
        @Test
        void shouldNotAddCardIfNameIsBlank() throws Exception {
            when(collectionRepository.fetchCollectionByCollectionId(1)).thenReturn(TestHelper.collection());
            Result<Card> result = collectionService.addCardToCollection("",TestHelper.collection());
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
}