package mtgcollection.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mysql.cj.protocol.x.StatementExecuteOk;
import mtgcollection.TestHelper;
import mtgcollection.domain.CollectionService;
import mtgcollection.domain.DeckService;
import mtgcollection.dto.CardEditRequest;
import mtgcollection.model.Deck;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import mtgcollection.model.card.Card;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @MockitoBean
    CollectionService collectionService;

    @MockitoBean
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
            String token = JwtHandler.generateToken("a@a.com");
            when(deckService.fetchAllDecksByCollectionId(TestHelper.collection().getCollectionId())).thenReturn(List.of(TestHelper.user2Deck()));
            MockHttpServletRequestBuilder request = get("/api/decks/{collectionId}",1)
                    .header("authorization", token);
            mvc.perform(request).andExpect(status().isOk());
        }
    }

    @Nested
    class FetchDeckByDeckIdTest{
        @Test
        void shouldFetchDeckByDeckId() throws Exception{
            int deckThatExists = 2;
            String token = JwtHandler.generateToken("a@a.com");
            Result<Deck> expected = new Result<>();
            expected.setpayload(TestHelper.user2Deck());
            when(deckService.fetchDeckByDeckId(deckThatExists)).thenReturn(expected);
            MockHttpServletRequestBuilder request = get("/api/deck/{deckId}",deckThatExists)
                    .header("authorization",token);
            mvc.perform(request).andExpect(status().isOk());
        }
        @Test
        void shouldNotFetchDeckByIdWhereDeckDoesNotExist() throws Exception{
            int deckThatDoesNotExists = Integer.MAX_VALUE;
            String token = JwtHandler.generateToken("a@a.com");
            Result<Deck> expected = new Result<>();
            expected.addErrorMessage("Deck not found.", ResultType.NOT_FOUND);
            when(deckService.fetchDeckByDeckId(deckThatDoesNotExists)).thenReturn(expected);
            MockHttpServletRequestBuilder request = get("/api/deck/{deckId}",deckThatDoesNotExists)
                    .header("authorization", token);
            mvc.perform(request).andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateDeckTest{
        @Test
        void shouldCreateDeckInCollection() throws Exception {
            Deck deck = TestHelper.deckToCreate();
            int validCollectionId = 1;
            String token = JwtHandler.generateToken("a@a.com");
            Result<Deck> expected = new Result<>();
            expected.setpayload(deck);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            when(deckService.createDeckInCollection(deck, validCollectionId))
                    .thenReturn(expected);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck",validCollectionId)
                    .param("collectionId", String.valueOf(validCollectionId))
                    .header("authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(deck));
            mvc.perform(request)
                    .andExpect(status().isCreated());
        }
        @Test
        void shouldNotAddDeckIfCollectionIdDoesNotExist()throws Exception{
            Deck deck = TestHelper.deckToCreate();
            deck.setDateCreated(LocalDate.now().minusDays(5));
            String token = JwtHandler.generateToken("a@a.com");
            int validCollectionId = 1;
            Result<Deck> expected = new Result<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            expected.addErrorMessage("Creation date has to be today.",ResultType.INVALID);
            when(deckService.createDeckInCollection(deck,validCollectionId)).thenReturn(expected);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck",validCollectionId)
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(deck));
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotAddDeckIfCreationDateIsInFuture()throws Exception{
            Deck deck = TestHelper.deckToCreate();
            deck.setDateCreated(LocalDate.now().plusDays(5));
            String token = JwtHandler.generateToken("a@a.com");
            int validCollectionId = 1;
            Result<Deck> expected = new Result<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            expected.addErrorMessage("Creation date has to be today.",ResultType.INVALID);
            when(deckService.createDeckInCollection(deck,validCollectionId)).thenReturn(expected);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck",validCollectionId)
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(deck));
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotAddDeckIfCreationDateIsInPast()throws Exception{
            Deck deck = TestHelper.deckToCreate();
            deck.setDateCreated(LocalDate.now().minusDays(5));
            String token = JwtHandler.generateToken("a@a.com");
            int validCollectionId = 1;
            Result<Deck> expected = new Result<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            expected.addErrorMessage("Creation date has to be today.",ResultType.INVALID);
            when(deckService.createDeckInCollection(deck,validCollectionId)).thenReturn(expected);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck",validCollectionId)
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(deck));
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotAddDeckIfNameIsNull()throws Exception{
            Deck deck = TestHelper.deckToCreate();
            deck.setName("");
            String token = JwtHandler.generateToken("a@a.com");
            int validCollectionId = 1;
            Result<Deck> expected = new Result<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            expected.addErrorMessage("Name cannot be null.",ResultType.INVALID);
            when(deckService.createDeckInCollection(deck,validCollectionId)).thenReturn(expected);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck",validCollectionId)
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(deck));
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotAddDeckIfDeckIsNull()throws Exception{
            int validCollectionId = 1;
            String token = JwtHandler.generateToken("a@a.com");
            Result<Deck> expected = new Result<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            expected.addErrorMessage("Deck cannot be null.",ResultType.INVALID);
            when(deckService.createDeckInCollection(null,validCollectionId)).thenReturn(expected);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/collection/{collectionId}/deck",validCollectionId)
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", token)
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
            String token = JwtHandler.generateToken("a@a.com");
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.setpayload(deckToEdit);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isNoContent());
        }

        @Test
        void shouldNotUpdateDeckWhereDeckIDsDoNotMatch() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String token = JwtHandler.generateToken("a@a.com");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",2)
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotUpdateDeckWhereDateUpdatedDoesNotExist() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setDeckId(Integer.MAX_VALUE);
            ObjectMapper mapper = new ObjectMapper();
            String token = JwtHandler.generateToken("a@a.com");
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Deck does not exist.",ResultType.NOT_FOUND);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isNotFound());
        }

        @Test
        void shouldNotUpdateDeckWhereDateUpdatedIsInPast() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setDateUpdated(LocalDate.of(1990,12,1));
            ObjectMapper mapper = new ObjectMapper();
            String token = JwtHandler.generateToken("a@a.com");
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Updated date has to be today or in future.",ResultType.INVALID);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotUpdateDeckWhereDateCreatedIsBlank() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setDateCreated(null);
            String token = JwtHandler.generateToken("a@a.com");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Updated date cannot be null.",ResultType.INVALID);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotUpdateDeckWhereDateUpdatedIsBlank() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setDateUpdated(null);
            String token = JwtHandler.generateToken("a@a.com");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Updated date cannot be null.",ResultType.INVALID);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotUpdateDeckWhereNameIsBlank() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            deckToEdit.setName("");
            String token = JwtHandler.generateToken("a@a.com");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Deck> result = new Result<>();
            result.addErrorMessage("Name cannot be blank.",ResultType.INVALID);
            when(deckService.updateDeck(deckToEdit)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}",deckToEdit.getDeckId())
                            .header("authorization", token)
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
            String token = JwtHandler.generateToken("a@a.com");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Card> result = new Result<>();
            result.setpayload(TestHelper.blackLotus());
            when(deckService.addCardToDeck(deckToEdit,cardNameReal)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",deckToEdit.getDeckId(),cardNameReal)
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isCreated());
        }

        // Should not add card to deck where deckId path and body don't match
        @Test
        void shouldNotAddCardToDeckWhereDeckIdPathAndRequestBodyDontMatch() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String cardNameNotReal = "BLACK LOTUS PRIME";
            String token = JwtHandler.generateToken("a@a.com");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",Integer.MAX_VALUE,cardNameNotReal)
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        // Should not add card where card does not exist
        @Test
        void shouldNotAddCardToDeckWhereCardDoesNotExist() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String cardNameNotReal = "BLACK LOTUS PRIME";
            String token = JwtHandler.generateToken("a@a.com");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Card> result = new Result<>();
            result.addErrorMessage("Card not found",ResultType.NOT_FOUND);
            when(deckService.addCardToDeck(deckToEdit,cardNameNotReal)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",deckToEdit.getDeckId(),cardNameNotReal)
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isNotFound());
        }

        // Should not add card where deck does not exist
        @Test
        void shouldNotAddCardToDeckWhereDeckDoesNotExist() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String cardName = "BLACK LOTUS";
            String token = JwtHandler.generateToken("a@a.com");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Card> result = new Result<>();
            result.addErrorMessage("Deck not found",ResultType.NOT_FOUND);
            when(deckService.addCardToDeck(deckToEdit,cardName)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",deckToEdit.getDeckId(),cardName)
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isNotFound());
        }


        // Should not add card where cardDeck does note xist
        @Test
        void shouldNotAddCardToDeckWhereCardDeckDoesHasErrorAddingCardToDeck() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String token = JwtHandler.generateToken("a@a.com");
            String cardName = "BLACK LOTUS";
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Card> result = new Result<>();
            result.addErrorMessage("Error adding card to deck.",ResultType.INVALID);
            when(deckService.addCardToDeck(deckToEdit,cardName)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",deckToEdit.getDeckId(),cardName)
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deckToEditString);
            mvc.perform(request).andExpect(status().isBadRequest());
        }

        // Should not Add card where adding to deck fails
        @Test
        void shouldNotAddCardToDeckWhereUpdatingDeckFails() throws Exception {
            Deck deckToEdit = TestHelper.deckToEdit();
            String cardName = "BLACK LOTUS";
            String token = JwtHandler.generateToken("a@a.com");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String deckToEditString = mapper.writeValueAsString(deckToEdit);
            Result<Card> result = new Result<>();
            result.addErrorMessage("Error updating deck",ResultType.INVALID);
            when(deckService.addCardToDeck(deckToEdit,cardName)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.post("/api/deck/{deckId}/card/{cardName}",deckToEdit.getDeckId(),cardName)
                            .header("authorization", token)
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
            String token = JwtHandler.generateToken("a@a.com");
            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(cardEditRequest);

            Result<Integer> result = new Result<>();
            result.setpayload(1); // 1 row updated

            when(deckService.updateCardInADeck(validCardId, validDeckId, cardEditRequest.quantity()))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}/card/{cardId}", validDeckId, validCardId)
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent);

            mvc.perform(request).andExpect(status().isNoContent());
        }

        @Test
        void shouldNotUpdateCardInADeckWhenNotFound() throws Exception {
            int invalidDeckId = Integer.MAX_VALUE;
            int validCardId = 10;
            CardEditRequest cardEditRequest = new CardEditRequest(4);
            String token = JwtHandler.generateToken("a@a.com");

            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(cardEditRequest);

            Result<Integer> result = new Result<>();
            result.addErrorMessage("Deck or card not found.", ResultType.NOT_FOUND);

            when(deckService.updateCardInADeck(validCardId, invalidDeckId, cardEditRequest.quantity()))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}/card/{cardId}", invalidDeckId, validCardId)
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent);

            mvc.perform(request).andExpect(status().isNotFound());
        }

        @Test
        void shouldNotUpdateCardInADeckWhenTransactionFails() throws Exception {
            int validDeckId = 1;
            int validCardId = 10;
            CardEditRequest cardEditRequest = new CardEditRequest(-1); // Invalid quantity trigger
            String token = JwtHandler.generateToken("a@a.com");

            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(cardEditRequest);

            Result<Integer> result = new Result<>();
            result.addErrorMessage("Quantity must be greater than zero.", ResultType.INVALID);

            when(deckService.updateCardInADeck(validCardId, validDeckId, cardEditRequest.quantity()))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.put("/api/deck/{deckId}/card/{cardId}", validDeckId, validCardId)
                            .header("authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent);

            mvc.perform(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldNotUpdateCardInADeckWhenMissingAuthHeader() throws Exception {
            int validDeckId = 1;
            int validCardId = 10;
            CardEditRequest cardEditRequest = new CardEditRequest(4);
            String token = JwtHandler.generateToken("a@a.com");

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
            String token = JwtHandler.generateToken("a@a.com");
            Result<Integer> result = new Result<>();
            result.setpayload(1);

            when(deckService.removeCardFromDeck(validCardId, validDeckId))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}/card/{cardId}", validDeckId, validCardId)
                            .header("authorization", token);

            mvc.perform(request).andExpect(status().isNoContent());
        }

        @Test
        void shouldNotRemoveCardFromADeckWhenNotFound() throws Exception {
            int invalidDeckId = Integer.MAX_VALUE;
            int validCardId = 10;
            String token = JwtHandler.generateToken("a@a.com");
            Result<Integer> result = new Result<>();
            result.addErrorMessage("Card or deck not found.", ResultType.NOT_FOUND);

            when(deckService.removeCardFromDeck(validCardId, invalidDeckId))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}/card/{cardId}", invalidDeckId, validCardId)
                            .header("authorization", token);

            mvc.perform(request).andExpect(status().isNotFound());
        }

        @Test
        void shouldNotRemoveCardFromADeckWhenTransactionFails() throws Exception {
            int validDeckId = 1;
            int validCardId = 10;
            String token = JwtHandler.generateToken("a@a.com");
            Result<Integer> result = new Result<>();
            result.addErrorMessage("Error removing card from deck.", ResultType.INVALID);

            when(deckService.removeCardFromDeck(validCardId, validDeckId))
                    .thenReturn(result);

            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}/card/{cardId}", validDeckId, validCardId)
                            .header("authorization", token);

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
            String token = JwtHandler.generateToken("a@a.com");
            Result<Integer> result = new Result<>();
            result.setpayload(validDeckID);
            when(deckService.removeDeck(validDeckID)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}",validDeckID)
                            .header("authorization", token);
            mvc.perform(request).andExpect(status().isNoContent());
        }

        @Test
        void shouldNotDeleteDeckWhenCardIsNotFound() throws Exception {
            int inValidDeckID = Integer.MAX_VALUE;
            String token = JwtHandler.generateToken("a@a.com");
            Result<Integer> result = new Result<>();
            result.addErrorMessage("Deck not found.",ResultType.NOT_FOUND);
            when(deckService.removeDeck(inValidDeckID)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}",inValidDeckID)
                            .header("authorization", token);
            mvc.perform(request).andExpect(status().isNotFound());
        }

        @Test
        void shouldNotDeleteWhenAnotherTransactionFails() throws Exception {
            int validDeckID = 1;
            Result<Integer> result = new Result<>();
            String token = JwtHandler.generateToken("a@a.com");
            result.addErrorMessage("Error deleting deck.",ResultType.INVALID);
            when(deckService.removeDeck(validDeckID)).thenReturn(result);
            MockHttpServletRequestBuilder request =
                    MockMvcRequestBuilders.delete("/api/deck/{deckId}",validDeckID)
                            .header("authorization", token);
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }
}