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

    @GetMapping("/{collectionId}")
    public ResponseEntity<?> fetchAllCardsByUserId(@RequestHeader Map<String,String> headers,@PathVariable int collectionId) throws JsonProcessingException {
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }

        // Extract json from string
        String userAuthJson = headers.get("authorization");

        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson,LoggedInUser.class);
        return new ResponseEntity<>(collectionService.fetchAllCardsByCollection(collectionId),HttpStatus.OK);
    }

    @PostMapping("/{collectionId}")
    public ResponseEntity<?> addCardToCollection(@RequestHeader Map<String,String> headers, @RequestBody CardAddRequest request, @PathVariable int collectionId) throws JsonProcessingException, InterruptedException {
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }
        // Extract json from string
        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson,LoggedInUser.class);
        String cardName = request.name();
        Result<Card> result = collectionService.addCardToCollection(cardName,collectionId);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getpayload(),HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{collectionId}/card/{cardId}")
    public ResponseEntity<?> updateCardFromCollection(@RequestHeader Map<String,String> headers, @PathVariable int cardId, @RequestBody CardEditRequest request, @PathVariable int collectionId) throws JsonProcessingException {
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }
        // Extract json from string
        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson,LoggedInUser.class);
        int quantity = request.quantity();
        Result<Integer> result = collectionService.updateCardInCollection(cardId,collectionId,quantity);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{collectionId}/card/{cardId}")
    public ResponseEntity<?> removeCardFromCollection(@RequestHeader Map<String,String> headers, @PathVariable int cardId, @PathVariable int collectionId) throws JsonProcessingException {
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }
        // Extract json from string
        String userAuthJson = headers.get("authorization");
        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson,LoggedInUser.class);
        Result<Integer> result = collectionService.removeCardFromCollection(cardId,collectionId);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}
