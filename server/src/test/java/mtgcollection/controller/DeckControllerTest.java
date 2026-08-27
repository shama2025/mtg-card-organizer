package mtgcollection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mtgcollection.TestHelper;
import mtgcollection.domain.CollectionService;
import mtgcollection.domain.DeckService;
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
            MockHttpServletRequestBuilder request = get("/api/collection");
            // assert
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class FetchDeckByCollectionID{
        @Test
        void shouldFetchDeckByCollectionID() throws Exception {
            // Act
            when(collectionService.findCollectionByUserId(1)).thenReturn(TestHelper.collection());
            when(deckService.fetchAllDecksByCollectionId(TestHelper.collection().getCollectionId())).thenReturn(List.of(TestHelper.user2Deck()));
            MockHttpServletRequestBuilder request = get("/api/deck")
                    .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}");            // assert
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
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/deck")
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
                    MockMvcRequestBuilders.post("/api/deck")
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
                    MockMvcRequestBuilders.post("/api/deck")
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
                    MockMvcRequestBuilders.post("/api/deck")
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
                    MockMvcRequestBuilders.post("/api/deck")
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
                    MockMvcRequestBuilders.post("/api/deck")
                            .param("collectionId", String.valueOf(validCollectionId))
                            .header("authorization", "{\"id\": \"1\",\"email\": \"a@a.com\"}")
                            .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request).andExpect(status().isBadRequest());
        }
    }
}