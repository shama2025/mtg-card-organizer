package mtgcollection.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mtgcollection.domain.CollectionService;
import mtgcollection.domain.DeckService;
import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.Collection;
import mtgcollection.model.Deck;
import mtgcollection.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deck")
@CrossOrigin
public class DeckController {

    private final DeckService deckService;
    private final CollectionService collectionService;

    public DeckController(DeckService deckService, CollectionService collectionService) {
        this.deckService = deckService;
        this.collectionService = collectionService;
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<?> fetchAllDecksByCollectionId(@RequestHeader Map<String,String> headers, @PathVariable int collectionId) throws JsonProcessingException {
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }

        // Extract json from string
        String userAuthJson = headers.get("authorization");

        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson,LoggedInUser.class);
        return new ResponseEntity<>(deckService.fetchAllDecksByCollectionId(collectionId),HttpStatus.OK);
    }

    @GetMapping("/{deckId}")
    public ResponseEntity<?> fetchDeckByDeckId(@PathVariable int deckId){
        Result<Deck> result = deckService.fetchDeckByDeckId(deckId);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getpayload(),HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }
}
