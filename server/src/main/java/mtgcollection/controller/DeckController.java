package mtgcollection.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mtgcollection.domain.CollectionService;
import mtgcollection.domain.DeckService;
import mtgcollection.dto.CardEditRequest;
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
@RequestMapping("/api")
@CrossOrigin
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("/decks/{collectionId}")
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

    @GetMapping("/deck/{deckId}")
    public ResponseEntity<?> fetchDeckByDeckId(@PathVariable int deckId){
        Result<Deck> result = deckService.fetchDeckByDeckId(deckId);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getpayload(),HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @PostMapping("/collection/{collectionId}/deck")
    public ResponseEntity<?> createDeck(@RequestHeader Map<String, String> headers, @PathVariable int collectionId,
                                        @RequestBody Deck deck
    ) throws JsonProcessingException {
        if (headers.get("authorization") == null) {
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }

        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson, LoggedInUser.class);

        Result<Deck> result = deckService.createDeckInCollection(deck, collectionId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getpayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/deck/{deckId}")
    public ResponseEntity<?> updateDeck(@RequestHeader Map<String,String> headers, @PathVariable int deckId, @RequestBody Deck deck) throws JsonProcessingException{
        if(deckId != deck.getDeckId()){
            return new ResponseEntity<>(List.of("Invalid deck id."), HttpStatus.BAD_REQUEST);
        }

        if (headers.get("authorization") == null) {
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }

        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson, LoggedInUser.class);

        Result<Deck> result = deckService.updateDeck(deck);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @PostMapping("/deck/{deckId}/card/{cardName}")
    public ResponseEntity<?> addCardToDeck(@RequestHeader Map<String,String> headers,
                                           @RequestBody Deck deck,
                                           @PathVariable int deckId,
                                           @PathVariable String cardName) throws JsonProcessingException, InterruptedException {
        if(deckId != deck.getDeckId()){
            return new ResponseEntity<>(List.of("Invalid deck id."), HttpStatus.BAD_REQUEST);
        }

        if (headers.get("authorization") == null) {
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }

        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson, LoggedInUser.class);

        Result<Deck> result = deckService.addCardToDeck(deck,cardName);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/deck/{deckId}/card/{cardId}")
    public ResponseEntity<?> updateCardInADeck(@RequestHeader Map<String,String> headers,
                                               @RequestBody CardEditRequest request,
                                               @PathVariable int deckId,
                                               @PathVariable int cardId
                                               ) throws JsonProcessingException {

        if (headers.get("authorization") == null) {
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }

        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson, LoggedInUser.class);

        Result<Integer> result = deckService.updateCardInADeck(cardId,deckId,request.quantity());
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/deck/{deckId}/card/{cardId}")
    public ResponseEntity<?> removeCardFromADeck(@RequestHeader Map<String,String> headers,
                                               @PathVariable int deckId,
                                               @PathVariable int cardId
    ) throws JsonProcessingException {

        if (headers.get("authorization") == null) {
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }

        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson, LoggedInUser.class);

        Result<Integer> result = deckService.removeCardFromDeck(cardId,deckId);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/deck/{deckId}")
    public ResponseEntity<?> deleteDeck(@RequestHeader Map<String,String> headers, @PathVariable int deckId) throws JsonProcessingException {
        if (headers.get("authorization") == null) {
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }

        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson, LoggedInUser.class);

        Result<Integer> result = deckService.removeDeck(deckId);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}
