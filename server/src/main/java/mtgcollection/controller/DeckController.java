package mtgcollection.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mtgcollection.domain.CollectionService;
import mtgcollection.domain.DeckService;
import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.Collection;
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

    @GetMapping
    public ResponseEntity<?> fetchAllDecksByCollectionId(@RequestHeader Map<String,String> headers) throws JsonProcessingException {
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"), HttpStatus.BAD_REQUEST);
        }

        // Extract json from string
        String userAuthJson = headers.get("authorization");

        ObjectMapper mapper = new ObjectMapper();
        LoggedInUser user = mapper.readValue(userAuthJson,LoggedInUser.class);
        Collection collection = collectionService.findCollectionByUserId(user.id());
        return new ResponseEntity<>(deckService.fetchAllDecksByCollectionId(collection.getCollectionId()),HttpStatus.OK);
    }
}
