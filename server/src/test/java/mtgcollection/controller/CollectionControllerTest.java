package mtgcollection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mtgcollection.TestHelper;
import mtgcollection.domain.CollectionService;
import mtgcollection.dto.CardAddRequest;
import mtgcollection.dto.CardEditRequest;
import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.Collection;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import mtgcollection.model.card.Card;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CollectionControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    CollectionService collectionService;

    @Nested
    class ErrorTests{
        @Test
        void shouldReturnBadRequestResponse() throws Exception {
            // Act
            int validCollectionId = 1;
            when(collectionService.fetchAllCardsByCollection(1)).thenReturn(TestHelper.cardList());
            MockHttpServletRequestBuilder request = get("/api/collection/{collectionId}",validCollectionId);
            // assert
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class FetchCollectionByUserId{
        @Test
        void shouldFetchCollectionByUserID() throws Exception {
            // Act
            int validCollectionId = 1;
            when(collectionService.findCollectionByUserId(1)).thenReturn(TestHelper.collection());
            when(collectionService.fetchAllCardsByCollection(1)).thenReturn(TestHelper.cardList());
            MockHttpServletRequestBuilder request = get("/api/collection/{collectionId}",validCollectionId)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");
            mvc.perform(request).andExpect(status().isOk());
        }
    }

    @Nested
    class AddCardToCollectionTest{
        // Should add card that is present in db
        @Test
        void shouldAddCardThatIsInDataBase() throws Exception {
            CardAddRequest cardRequest = new CardAddRequest("lightning bolt");
            int validCollection = 1;
            ObjectMapper mapper = new ObjectMapper();
            String cardRequestString = mapper.writeValueAsString(cardRequest);
            Result<Card> result = new Result<>();
            result.setpayload(TestHelper.lightningBolt());
            when(collectionService.addCardToCollection(cardRequest.name(),validCollection)).thenReturn(result);
            MockHttpServletRequestBuilder request = post("/api/collection/{collectionId}", validCollection)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(cardRequestString);
            mvc.perform(request).andExpect(status().isCreated());
        }
        // Should add card that is not present in db
        @Test
        void shouldAddCardThatIsNotPresentInDataBase() throws Exception{
            CardAddRequest cardRequest = new CardAddRequest("black lotus");
            int validCollection = 1;
            ObjectMapper mapper = new ObjectMapper();
            String cardRequestString = mapper.writeValueAsString(cardRequest);
            Result<Card> result = new Result<>();
            result.setpayload(TestHelper.blackLotus());
            when(collectionService.addCardToCollection(cardRequest.name(),validCollection)).thenReturn(result);
            MockHttpServletRequestBuilder request = post("/api/collection/{collectionId}", validCollection)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(cardRequestString);
            mvc.perform(request).andExpect(status().isCreated());
        }

        // Collection does not exist for user
        @Test
        void shouldNotAddCardToNonExistentCollection() throws Exception {
            CardAddRequest cardRequest = new CardAddRequest("black lotus");
            int nonExistentCollection = Integer.MAX_VALUE;
            ObjectMapper mapper = new ObjectMapper();
            String cardRequestString = mapper.writeValueAsString(cardRequest);
            Result<Card> result = new Result<>();
            result.addErrorMessage("Collection was not found.", ResultType.NOT_FOUND);
            when(collectionService.addCardToCollection(cardRequest.name(),nonExistentCollection)).thenReturn(result);
            MockHttpServletRequestBuilder request = post("/api/collection/{collectionId}",nonExistentCollection)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(cardRequestString);
            mvc.perform(request).andExpect(status().isNotFound());
        }

        // card does not exist
        @Test
        void shouldNotAddNonExistentCard() throws Exception {
            CardAddRequest cardRequest = new CardAddRequest("sdfhksjdlkfsda");
            int validCollection = TestHelper.collection().getCollectionId();
            ObjectMapper mapper = new ObjectMapper();
            String cardRequestString = mapper.writeValueAsString(cardRequest);
            Result<Card> result = new Result<>();
            result.addErrorMessage("Card was not found.", ResultType.NOT_FOUND);
            when(collectionService.addCardToCollection(cardRequest.name(),validCollection)).thenReturn(result);
            MockHttpServletRequestBuilder request = post("/api/collection/{collectionId}",validCollection)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(cardRequestString);
            mvc.perform(request).andExpect(status().isNotFound());
        }

        // Missing input for cardName
        @Test
        void shouldNotAddCardWithNoName() throws Exception {
            CardAddRequest cardRequest = new CardAddRequest("");
            int nonExistentCollection = Integer.MAX_VALUE;
            ObjectMapper mapper = new ObjectMapper();
            String cardRequestString = mapper.writeValueAsString(cardRequest);
            Result<Card> result = new Result<>();
            result.addErrorMessage("Card name cannot be blank.", ResultType.NOT_FOUND);
            when(collectionService.addCardToCollection(cardRequest.name(),nonExistentCollection)).thenReturn(result);
            MockHttpServletRequestBuilder request = post("/api/collection/{collectionId}",nonExistentCollection)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(cardRequestString);
            mvc.perform(request).andExpect(status().isNotFound());
        }
    }

    @Nested
    class RemoveCardFromCollectionTest{
        // Should remove card
        @Test
        void shouldRemoveCardFromCollection() throws Exception{
            Result<Integer> expected = new Result<>();
            int cardThatDoesExist = TestHelper.lightningBolt().getId();
            int collectionThatDoesExist = TestHelper.collection().getCollectionId();
            LoggedInUser user = TestHelper.loggedInUser();
            expected.setpayload(cardThatDoesExist);
            when(collectionService.findCollectionByUserId(user.id())).thenReturn(TestHelper.collection());
            when(collectionService.removeCardFromCollection(cardThatDoesExist,collectionThatDoesExist)).thenReturn(expected);
            MockHttpServletRequestBuilder request = delete("/api/collection/{collectionId}/card/{cardId}",collectionThatDoesExist,cardThatDoesExist)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");
            mvc.perform(request).andExpect(status().isNoContent());
        }

        // Should not remove card when collection does not exist
        @Test
        void shouldNotRemoveCardFromCollectionWhenCollectionDoesNotExist() throws Exception{
            Result<Integer> expected = new Result<>();
            int cardThatDoesExist = TestHelper.lightningBolt().getId();
            int invalidCollection = Integer.MAX_VALUE;
            expected.addErrorMessage("Card was not found.", ResultType.NOT_FOUND);
            when(collectionService.removeCardFromCollection(cardThatDoesExist,invalidCollection)).thenReturn(expected);
            MockHttpServletRequestBuilder request = delete("/api/collection/{collectionId}/card/{cardId}",invalidCollection,cardThatDoesExist)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");
            mvc.perform(request).andExpect(status().isNotFound());
        }

        // Should not remove card when card does not exist
        @Test
        void shouldNotRemoveCardFromCollectionWhenCardDoesNotExist() throws Exception {
            Result<Integer> expected = new Result<>();
            int cardThatDoesNotExist = Integer.MAX_VALUE;
            int validCollection = TestHelper.collection().getCollectionId();
            expected.addErrorMessage("Card was not found.", ResultType.NOT_FOUND);
            when(collectionService.removeCardFromCollection(cardThatDoesNotExist,validCollection)).thenReturn(expected);
            MockHttpServletRequestBuilder request = delete("/api/collection/{collectionId}/card/{cardId}",validCollection,cardThatDoesNotExist)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");
            mvc.perform(request).andExpect(status().isNotFound());
        }
    }

    @Nested
    class UpdateCardFromCollectionTest{
        // Should update card
        @Test
        void shouldUpdateCardFromCollection() throws Exception{
            CardEditRequest cardEditRequest = new CardEditRequest(5);
            Result<Integer> expected = new Result<>();
            int cardThatDoesExist = TestHelper.lightningBolt().getId();
            int collectionThatDoesExist = TestHelper.collection().getCollectionId();
            LoggedInUser user = TestHelper.loggedInUser();
            expected.setpayload(cardThatDoesExist);

            ObjectMapper mapper = new ObjectMapper();
            String cardEditRequestString = mapper.writeValueAsString(cardEditRequest);

            when(collectionService.findCollectionByUserId(user.id())).thenReturn(TestHelper.collection());
            when(collectionService.updateCardInCollection(cardThatDoesExist,collectionThatDoesExist,cardEditRequest.quantity())).thenReturn(expected);

            MockHttpServletRequestBuilder request = put("/api/collection/{collectionId}/card/{cardId}",collectionThatDoesExist,cardThatDoesExist)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .contentType("application/json")
                    .content(cardEditRequestString);
            mvc.perform(request).andExpect(status().isNoContent());
        }

        // should not update card when collection does not exist
        @Test
        void shouldNotUpdateCardFromCollectionWhenCollectionDoesNotExist() throws Exception{
            CardEditRequest cardEditRequest = new CardEditRequest(-10);
            int cardThatDoesNotExist = TestHelper.lightningBolt().getId();
            int collectionThatDoesNotExist = Integer.MAX_VALUE;
            Result<Integer> expected = new Result<>();
            expected.addErrorMessage("Collection does Not exist.", ResultType.NOT_FOUND);

            ObjectMapper mapper = new ObjectMapper();
            String cardEditRequestString = mapper.writeValueAsString(cardEditRequest);

            when(collectionService.updateCardInCollection(cardThatDoesNotExist,collectionThatDoesNotExist,cardEditRequest.quantity())).thenReturn(expected);

            MockHttpServletRequestBuilder request = put("/api/collection/{collectionId}/card/{cardId}",collectionThatDoesNotExist,cardThatDoesNotExist)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .contentType("application/json")
                    .content(cardEditRequestString);
            mvc.perform(request).andExpect(status().isNotFound());
        }
        // should not update card when card does not exist
        @Test
        void shouldNotUpdateCardFromCollectionWhenCardDoesNotExist() throws Exception {
            CardEditRequest cardEditRequest = new CardEditRequest(-10);
            int cardThatDoesNotExist = Integer.MAX_VALUE;
            Collection collection = TestHelper.collection();
            Result<Integer> expected = new Result<>();
            expected.addErrorMessage("Card Does Not exist.", ResultType.NOT_FOUND);

            ObjectMapper mapper = new ObjectMapper();
            String cardEditRequestString = mapper.writeValueAsString(cardEditRequest);

            when(collectionService.updateCardInCollection(cardThatDoesNotExist,collection.getCollectionId(),cardEditRequest.quantity())).thenReturn(expected);

            MockHttpServletRequestBuilder request = put("/api/collection/{collectionId}/card/{cardId}",collection.getCollectionId(),cardThatDoesNotExist)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .contentType("application/json")
                    .content(cardEditRequestString);
            mvc.perform(request).andExpect(status().isNotFound());
        }
        // should not update card when quantity is below 0
        @Test
        void shouldNotUpdateCardWhenQuantityIsBelowZero() throws Exception{
            CardEditRequest cardEditRequest = new CardEditRequest(-10);
            int cardThatExist = TestHelper.lightningBolt().getId();
            Collection collection = TestHelper.collection();
            Result<Integer> expected = new Result<>();
            expected.addErrorMessage("Quantity cannot be below 0.", ResultType.INVALID);

            ObjectMapper mapper = new ObjectMapper();
            String cardEditRequestString = mapper.writeValueAsString(cardEditRequest);

            when(collectionService.updateCardInCollection(cardThatExist,collection.getCollectionId(),cardEditRequest.quantity())).thenReturn(expected);

            MockHttpServletRequestBuilder request = put("/api/collection/{collectionId}/card/{cardId}",collection.getCollectionId(),cardThatExist)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .contentType("application/json")
                    .content(cardEditRequestString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }

}