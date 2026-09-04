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
        ResponseEntity<?> responseEntity = AuthHandler.auth(headers);
        if(!(responseEntity == null)){
            return responseEntity;
        }
        return new ResponseEntity<>(collectionService.fetchAllCardsByCollection(collectionId),HttpStatus.OK);
    }

    @PostMapping("/{collectionId}")
    public ResponseEntity<?> addCardToCollection(@RequestHeader Map<String,String> headers, @RequestBody CardAddRequest request, @PathVariable int collectionId) throws JsonProcessingException, InterruptedException {
        ResponseEntity<?> responseEntity = AuthHandler.auth(headers);
        if(!(responseEntity == null)){
            return responseEntity;
        }
        String cardName = request.name();
        Result<Card> result = collectionService.addCardToCollection(cardName,collectionId);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getpayload(),HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{collectionId}/card/{cardId}")
    public ResponseEntity<?> updateCardFromCollection(@RequestHeader Map<String,String> headers, @PathVariable int cardId, @RequestBody CardEditRequest request, @PathVariable int collectionId) throws JsonProcessingException {
        ResponseEntity<?> responseEntity = AuthHandler.auth(headers);
        if(!(responseEntity == null)){
            return responseEntity;
        }
        int quantity = request.quantity();
        Result<Integer> result = collectionService.updateCardInCollection(cardId,collectionId,quantity);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{collectionId}/card/{cardId}")
    public ResponseEntity<?> removeCardFromCollection(@RequestHeader Map<String,String> headers, @PathVariable int cardId, @PathVariable int collectionId) throws JsonProcessingException {
        ResponseEntity<?> responseEntity = AuthHandler.auth(headers);
        if(!(responseEntity == null)){
            return responseEntity;
        }
        Result<Integer> result = collectionService.removeCardFromCollection(cardId,collectionId);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}
