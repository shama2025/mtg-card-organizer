package mtgcollection.controller;

import mtgcollection.TestHelper;
import mtgcollection.domain.CollectionService;
import mtgcollection.domain.DeckService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
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

}