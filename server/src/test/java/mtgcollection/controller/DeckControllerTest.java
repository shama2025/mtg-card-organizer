package mtgcollection.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mtgcollection.TestHelper;
import mtgcollection.domain.CollectionService;
import mtgcollection.domain.DeckService;
import mtgcollection.dto.CardEditRequest;
import mtgcollection.model.Deck;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DeckControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    CollectionService collectionService;

    @MockBean
    DeckService deckService;

    @Nested
    class ErrorTests{
        @Test
        void shouldReturnBadRequestResponse() throws Exception {
            // Act
            when(collectionService.fetchAllCardsByCollection(1)).thenReturn(TestHelper.cardList());
            MockHttpServletRequestBuilder request = get("/api/collection/{collectionId}",0);
            // assert
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class FetchDeckByCollectionID{
        @Test
        void shouldFetchDeckByCollectionID() throws Exception {
            // Act
            when(deckService.fetchAllDecksByCollectionId(TestHelper.collection().getCollectionId())).thenReturn(List.of(TestHelper.user2Deck()));
            MockHttpServletRequestBuilder request = get("/api/decks/{collectionId}",1)
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");
            mvc.perform(request).andExpect(status().isOk());
        }
    }

    @Nested
    class FetchDeckByDeckIdTest{
        @Test
        void shouldFetchDeckByDeckId() throws Exception{
            int deckThatExists = 2;
            Result<Deck> expected = new Result<>();
            expected.setpayload(TestHelper.user2Deck());
            when(deckService.fetchDeckByDeckId(deckThatExists)).thenReturn(expected);
            MockHttpServletRequestBuilder request = get("/api/deck/{deckId}",deckThatExists);
            mvc.perform(request).andExpect(status().isOk());
        }
        @Test
        void shouldNotFetchDeckByIdWhereDeckDoesNotExist() throws Exception{
            int deckThatDoesNotExists = Integer.MAX_VALUE;
            Result<Deck> expected = new Result<>();
            expected.addErrorMessage("Deck not found.", ResultType.NOT_FOUND);
            when(deckService.fetchDeckByDeckId(deckThatDoesNotExists)).thenReturn(expected);
            MockHttpServletRequestBuilder request = get("/api/deck/{deckId}",deckThatDoesNotExists);
            mvc.perform(request).andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateDeckTest{
        @Test
        void shouldCreateDeckInCollection() throws Exception {
            Deck deck = TestHelper.deckToCreate();
            int validCollectionId = 1;
            Result<Deck> expected = new Result<>();
            expected.setpayload(deck);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            when(deckService.createDeckInCollection(deck, validCollectionId))
                    .thenReturn(expected);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck/{deckId}",validCollectionId,deck.getDeckId())
                    .param("collectionId", String.valueOf(validCollectionId))
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(deck));
            mvc.perform(request)
                    .andExpect(status().isCreated());
        }
        @Test
        void shouldNotAddDeckIfCollectionIdDoesNotExist()throws Exception{
            Deck deck = TestHelper.deckToCreate();
            deck.setDateCreated(LocalDate.now().minusDays(5));
            int validCollectionId = 1;
            Result<Deck> expected = new Result<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            expected.addErrorMessage("Creation date has to be today.",ResultType.INVALID);
            when(deckService.createDeckInCollection(deck,validCollectionId)).thenReturn(expected);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck/{deckId}",validCollectionId,deck.getDeckId())
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(deck));
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotAddDeckIfCreationDateIsInFuture()throws Exception{
            Deck deck = TestHelper.deckToCreate();
            deck.setDateCreated(LocalDate.now().plusDays(5));
            int validCollectionId = 1;
            Result<Deck> expected = new Result<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            expected.addErrorMessage("Creation date has to be today.",ResultType.INVALID);
            when(deckService.createDeckInCollection(deck,validCollectionId)).thenReturn(expected);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck/{deckId}",validCollectionId,deck.getDeckId())
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(deck));
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotAddDeckIfCreationDateIsInPast()throws Exception{
            Deck deck = TestHelper.deckToCreate();
            deck.setDateCreated(LocalDate.now().minusDays(5));
            int validCollectionId = 1;
            Result<Deck> expected = new Result<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            expected.addErrorMessage("Creation date has to be today.",ResultType.INVALID);
            when(deckService.createDeckInCollection(deck,validCollectionId)).thenReturn(expected);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck/{deckId}",validCollectionId,deck.getDeckId())
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(deck));
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotAddDeckIfNameIsNull()throws Exception{
            Deck deck = TestHelper.deckToCreate();
            deck.setName("");
            int validCollectionId = 1;
            Result<Deck> expected = new Result<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            expected.addErrorMessage("Name cannot be null.",ResultType.INVALID);
            when(deckService.createDeckInCollection(deck,validCollectionId)).thenReturn(expected);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck/{deckId}",validCollectionId,deck.getDeckId())
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(deck));
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotAddDeckIfDeckIsNull()throws Exception{
            int validCollectionId = 1;
            Result<Deck> expected = new Result<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            expected.addErrorMessage("Deck cannot be null.",ResultType.INVALID);
            when(deckService.createDeckInCollection(null,validCollectionId)).thenReturn(expected);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck/{deckId}",validCollectionId,0)
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateDeckTest{
        @Test
        void shouldUpdateDeck() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.setpayload(deckToEdit);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isNoContent());
        }

        @Test
        void shouldNotUpdateDeckWhereDeckIDsDoNotMatch() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",2)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotUpdateDeckWhereDateUpdatedDoesNotExist() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setDeckId(Integer.MAX_VALUE);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Deck does not exist.",ResultType.NOT_FOUND);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isNotFound());
        }

        @Test
        void shouldNotUpdateDeckWhereDateUpdatedIsInPast() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setDateUpdated(LocalDate.of(1990,12,1));
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Updated date has to be today or in future.",ResultType.INVALID);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotUpdateDeckWhereDateCreatedIsBlank() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setDateCreated(null);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Updated date cannot be null.",ResultType.INVALID);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotUpdateDeckWhereDateUpdatedIsBlank() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setDateUpdated(null);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Updated date cannot be null.",ResultType.INVALID);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotUpdateDeckWhereNameIsBlank() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setName("");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Name cannot be blank.",ResultType.INVALID);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

    }

    @Nested
    class AddCardToDeckTest{
        // Should add card to deck
        @Test
        void shouldAddCardToDeck() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String cardNameReal = "BLACK LOTUS";
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.setpayload(deckToEdit);
            when(deckService.addCardToDeck(deckToEdit,cardNameReal)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",deckToEdit.getDeckId(),cardNameReal)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isNoContent());
        }

        // Should not add card to deck where deckId path and body don't match
        @Test
        void shouldNotAddCardToDeckWhereDeckIdPathAndRequestBodyDontMatch() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String cardNameNotReal = "BLACK LOTUS PRIME";
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",Integer.MAX_VALUE,cardNameNotReal)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        // Should not add card where card does not exist
        @Test
        void shouldNotAddCardToDeckWhereCardDoesNotExist() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String cardNameNotReal = "BLACK LOTUS PRIME";
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Card not found",ResultType.NOT_FOUND);
            when(deckService.addCardToDeck(deckToEdit,cardNameNotReal)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",deckToEdit.getDeckId(),cardNameNotReal)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isNotFound());
        }

        // Should not add card where deck does not exist
        @Test
        void shouldNotAddCardToDeckWhereDeckDoesNotExist() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String cardName = "BLACK LOTUS";
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Deck not found",ResultType.NOT_FOUND);
            when(deckService.addCardToDeck(deckToEdit,cardName)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",deckToEdit.getDeckId(),cardName)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isNotFound());
        }


        // Should not add card where cardDeck does note xist
        @Test
        void shouldNotAddCardToDeckWhereCardDeckDoesHasErrorAddingCardToDeck() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String cardName = "BLACK LOTUS";
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Error adding card to deck.",ResultType.INVALID);
            when(deckService.addCardToDeck(deckToEdit,cardName)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",deckToEdit.getDeckId(),cardName)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        // Should not Add card where adding to deck fails
        @Test
        void shouldNotAddCardToDeckWhereUpdatingDeckFails() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String cardName = "BLACK LOTUS";
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Error updating deck",ResultType.INVALID);
            when(deckService.addCardToDeck(deckToEdit,cardName)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",deckToEdit.getDeckId(),cardName)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }
    @Nested
    class UpdateCardInADeckTest {

        @Test
        void shouldUpdateCardInADeck() throws Exception {
            int validDeckId = 1;
            int validCardId = 10;
            CardEditRequest cardEditRequest = new CardEditRequest(4); // updated quantity

            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(cardEditRequest);

            Result<Integer> result = new Result<>();
            result.setpayload(1); // 1 row updated

            when(deckService.updateCardInADeck(validCardId, validDeckId, cardEditRequest.quantity()))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}/card/{cardId}", validDeckId, validCardId)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent);

            mvc.perform(request).andExpect(status().isNoContent());
        }

        @Test
        void shouldNotUpdateCardInADeckWhenNotFound() throws Exception {
            int invalidDeckId = Integer.MAX_VALUE;
            int validCardId = 10;
            CardEditRequest cardEditRequest = new CardEditRequest(4);

            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(cardEditRequest);

            Result<Integer> result = new Result<>();
            result.addErrorMessage("Deck or card not found.", ResultType.NOT_FOUND);

            when(deckService.updateCardInADeck(validCardId, invalidDeckId, cardEditRequest.quantity()))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}/card/{cardId}", invalidDeckId, validCardId)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent);

            mvc.perform(request).andExpect(status().isNotFound());
        }

        @Test
        void shouldNotUpdateCardInADeckWhenTransactionFails() throws Exception {
            int validDeckId = 1;
            int validCardId = 10;
            CardEditRequest cardEditRequest = new CardEditRequest(-1); // Invalid quantity trigger

            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(cardEditRequest);

            Result<Integer> result = new Result<>();
            result.addErrorMessage("Quantity must be greater than zero.", ResultType.INVALID);

            when(deckService.updateCardInADeck(validCardId, validDeckId, cardEditRequest.quantity()))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}/card/{cardId}", validDeckId, validCardId)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent);

            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotUpdateCardInADeckWhenMissingAuthHeader() throws Exception {
            int validDeckId = 1;
            int validCardId = 10;
            CardEditRequest cardEditRequest = new CardEditRequest(4);

            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(cardEditRequest);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}/card/{cardId}", validDeckId, validCardId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent);

            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class RemoveCardFromADeckTest {

        @Test
        void shouldRemoveCardFromADeck() throws Exception {
            int validDeckId = 1;
            int validCardId = 10;

            Result<Integer> result = new Result<>();
            result.setpayload(1);

            when(deckService.removeCardFromDeck(validCardId, validDeckId))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}/card/{cardId}", validDeckId, validCardId)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");

            mvc.perform(request).andExpect(status().isNoContent());
        }

        @Test
        void shouldNotRemoveCardFromADeckWhenNotFound() throws Exception {
            int invalidDeckId = Integer.MAX_VALUE;
            int validCardId = 10;

            Result<Integer> result = new Result<>();
            result.addErrorMessage("Card or deck not found.", ResultType.NOT_FOUND);

            when(deckService.removeCardFromDeck(validCardId, invalidDeckId))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}/card/{cardId}", invalidDeckId, validCardId)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");

            mvc.perform(request).andExpect(status().isNotFound());
        }

        @Test
        void shouldNotRemoveCardFromADeckWhenTransactionFails() throws Exception {
            int validDeckId = 1;
            int validCardId = 10;

            Result<Integer> result = new Result<>();
            result.addErrorMessage("Error removing card from deck.", ResultType.INVALID);

            when(deckService.removeCardFromDeck(validCardId, validDeckId))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}/card/{cardId}", validDeckId, validCardId)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");

            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotRemoveCardFromADeckWhenMissingAuthHeader() throws Exception {
            int validDeckId = 1;
            int validCardId = 10;

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}/card/{cardId}", validDeckId, validCardId);

            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }


    @Nested
    class DeleteDeckTest{
        @Test
        void shouldDeleteDeck() throws Exception{
            int validDeckID = 1;
            Result<Integer> result = new Result<>();
            result.setpayload(validDeckID);
            when(deckService.removeDeck(validDeckID)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}",validDeckID)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");
            mvc.perform(request).andExpect(status().isNoContent());
        }

        @Test
        void shouldNotDeleteDeckWhenCardIsNotFound() throws Exception {
            int inValidDeckID = Integer.MAX_VALUE;
            Result<Integer> result = new Result<>();
            result.addErrorMessage("Deck not found.",ResultType.NOT_FOUND);
            when(deckService.removeDeck(inValidDeckID)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}",inValidDeckID)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");
            mvc.perform(request).andExpect(status().isNotFound());
        }

        @Test
        void shouldNotDeleteWhenAnotherTransactionFails() throws Exception {
            int validDeckID = 1;
            Result<Integer> result = new Result<>();
            result.addErrorMessage("Error deleting deck.",ResultType.INVALID);
            when(deckService.removeDeck(validDeckID)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}",validDeckID)
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }
}