package mtgcollection.controller;

import mtgcollection.TestHelper;
import mtgcollection.domain.CollectionService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
            when(collectionService.fetchAllCardsByCollection(1)).thenReturn(TestHelper.cardList());
            MockHttpServletRequestBuilder request = get("/api/collection");
            // assert
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class FetchCollectionByUserId{
        @Test
        void shouldFetchCollectionByUserID() throws Exception {
            // Act
            when(collectionService.findCollectionByUserId(1)).thenReturn(TestHelper.collection());
            when(collectionService.fetchAllCardsByCollection(1)).thenReturn(TestHelper.cardList());
            MockHttpServletRequestBuilder request = get("/api/collection")
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");            // assert
            mvc.perform(request).andExpect(status().isOk());
        }
    }

    @Nested
    class AddCardToCollectionTest{
        // Should add card that is present in db
        @Test
        void shouldAddCardThatIsInDataBase() throws Exception {
            Result<Card> expected = new Result<>();
            Collection collection = TestHelper.collection();
            LoggedInUser user = TestHelper.loggedInUser();
            Card cardToAdd = TestHelper.lightningBolt();
            cardToAdd.setId(5);
            expected.setpayload(cardToAdd);
            when(collectionService.findCollectionByUserId(user.id())).thenReturn(collection);
            when(collectionService.addCardToCollection("lightning bolt",collection)).thenReturn(expected);
            MockHttpServletRequestBuilder request = post("/api/collection")
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .content("lightning bolt");
            mvc.perform(request).andExpect(status().isCreated());
        }
        // Should add card that is not present in db
        @Test
        void shouldAddCardThatIsNotPresentInDataBase() throws Exception{
            Result<Card> expected = new Result<>();
            Collection collection = TestHelper.collection();
            LoggedInUser user = TestHelper.loggedInUser();
            Card cardToAdd = TestHelper.blackLotus();
            cardToAdd.setId(5);
            expected.setpayload(cardToAdd);
            when(collectionService.findCollectionByUserId(user.id())).thenReturn(collection);
            when(collectionService.addCardToCollection("black lotus",collection)).thenReturn(expected);
            MockHttpServletRequestBuilder request = post("/api/collection")
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .content("black lotus");
            mvc.perform(request).andExpect(status().isCreated());
        }

        // Collection does not exist for user
        @Test
        void shouldNotAddCardForNonExistentCollection() throws Exception {
            LoggedInUser user = TestHelper.loggedInUser();
            when(collectionService.findCollectionByUserId(user.id())).thenReturn(null);
            MockHttpServletRequestBuilder request = post("/api/collection")
                    .header("authorization", "{\"id\": \"999\",\"email\": \"a@a.com\"}")
                    .content("black lotus");
            mvc.perform(request).andExpect(status().isNotFound());
        }

        // card does not exist
        @Test
        void shouldNotAddNonExistentCard() throws Exception {
            Result<Card> expected = new Result<>();
            Collection collection = TestHelper.collection();
            LoggedInUser user = TestHelper.loggedInUser();
            expected.addErrorMessage("Card not found.", ResultType.NOT_FOUND);
            when(collectionService.findCollectionByUserId(user.id())).thenReturn(collection);
            when(collectionService.addCardToCollection("dsfjsfadljsja",collection)).thenReturn(expected);
            MockHttpServletRequestBuilder request = post("/api/collection")
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .content("dsfjsfadljsja");
            mvc.perform(request).andExpect(status().isNotFound());
        }

        // Collection not found
        @Test
        void shouldNotAddCardToNonExistentCollection() throws Exception {
            Result<Card> expected = new Result<>();
            Collection collection = TestHelper.collection();
            collection.setCollectionId(Integer.MAX_VALUE);
            LoggedInUser user = TestHelper.loggedInUser();
            expected.addErrorMessage("Collection was not found.", ResultType.NOT_FOUND);
            when(collectionService.findCollectionByUserId(user.id())).thenReturn(collection);
            when(collectionService.addCardToCollection("black lotus",collection)).thenReturn(expected);
            MockHttpServletRequestBuilder request = post("/api/collection")
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .content("black lotus");
            mvc.perform(request).andExpect(status().isNotFound());
        }

        // Missing input for cardName
        @Test
        void shouldNotAddCardWithNoName() throws Exception {
            Result<Card> expected = new Result<>();
            Collection collection = TestHelper.collection();
            LoggedInUser user = TestHelper.loggedInUser();
            expected.addErrorMessage("Card name cannot be empty.", ResultType.INVALID);
            when(collectionService.findCollectionByUserId(user.id())).thenReturn(collection);
            when(collectionService.addCardToCollection(null,collection)).thenReturn(expected);
            MockHttpServletRequestBuilder request = post("/api/collection")
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .content("");
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }

}