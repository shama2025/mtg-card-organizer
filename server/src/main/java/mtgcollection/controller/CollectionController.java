package mtgcollection.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mtgcollection.domain.CollectionService;
import mtgcollection.dto.CardEditRequest;
import mtgcollection.dto.CardAddRequest;
import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.Collection;
import mtgcollection.model.Result;
import mtgcollection.model.card.Card;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/collection")
@CrossOrigin
public class CollectionController {
    private CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @GetMapping
    public ResponseEntity<?> fetchAllCardsByUserId(@RequestHeader Map<String,String> headers) throws JsonProcessingException {
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }

        // Extract json from string
        String userAuthJson = headers.get("authorization");

        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson,LoggedInUser.class);
        Collection collection = collectionService.findCollectionByUserId(user.id());
        return new ResponseEntity<>(collectionService.fetchAllCardsByCollection(collection.getCollectionId()),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addCardToCollection(@RequestHeader Map<String,String> headers, @RequestBody CardAddRequest request) throws JsonProcessingException, InterruptedException {
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }
        // Extract json from string
        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson,LoggedInUser.class);
        Collection collection;
        try{
            collection = collectionService.findCollectionByUserId(user.id());
        }catch (EmptyResultDataAccessException ex){
           return new ResponseEntity<>(List.of("No collection associated with user."),HttpStatus.NOT_FOUND);
        }
        String cardName = request.name();
        Result<Card> result = collectionService.addCardToCollection(cardName,collection);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getpayload(),HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<?> updateCardFromCollection(@RequestHeader Map<String,String> headers, @PathVariable int cardId, @RequestBody CardEditRequest request) throws JsonProcessingException {
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }
        // Extract json from string
        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson,LoggedInUser.class);
        Collection collection = collectionService.findCollectionByUserId(user.id());
        if(collection == null){
            return new ResponseEntity<>(List.of("Collection does not correspond to user"),HttpStatus.NOT_FOUND);
        }
        Result<Integer> result = collectionService.updateCardInCollection(cardId,collection.getCollectionId(),request.quantity());
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> removeCardFromCollection(@RequestHeader Map<String,String> headers, @PathVariable int cardId) throws JsonProcessingException {
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }
        // Extract json from string
        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson,LoggedInUser.class);
        Collection collection = collectionService.findCollectionByUserId(user.id());
        if(collection == null){
            return new ResponseEntity<>(List.of("Collection does not correspond to user"),HttpStatus.NOT_FOUND);
        }
        Result<Integer> result = collectionService.removeCardFromCollection(cardId,collection.getCollectionId());
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}
