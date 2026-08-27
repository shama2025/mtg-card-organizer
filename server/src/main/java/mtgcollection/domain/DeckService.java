package mtgcollection.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import mtgcollection.data.interfaces.CollectionDeckRepository;
import mtgcollection.data.interfaces.CollectionRepository;
import mtgcollection.data.interfaces.DeckRepository;
import mtgcollection.model.CollectionDeck;
import mtgcollection.data.interfaces.CardDeckRepository;
import mtgcollection.data.interfaces.CardRepository;
import mtgcollection.data.interfaces.DeckRepository;
import mtgcollection.model.CardDeck;
import mtgcollection.model.Deck;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import mtgcollection.model.card.Card;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DeckService {

    private final DeckRepository deckRepository;

    private final CardDeckRepository cardDeckRepository;

    private final CardRepository cardRepository;

    private final CollectionRepository collectionRepository;

    public DeckService(DeckRepository deckRepository,CardDeckRepository cardDeckRepository,CardRepository cardRepository, CollectionRepository collectionRepository) {
        this.deckRepository = deckRepository;
        this.cardDeckRepository = cardDeckRepository;
        this.cardRepository = cardRepository;
        this.collectionRepository = collectionRepository;
    }

    public List<Deck> fetchAllDecksByCollectionId(int collectionId){return deckRepository.fetchAllDecksInACollection(collectionId);}

    public Result<Deck> fetchDeckByDeckId(int deckId){
        Result<Deck> result = new Result<>();
        List<Card> cardList = new ArrayList<>();
        Deck deck = deckRepository.fetchDeckByDeckId(deckId);
        if(deck == null){
            result.addErrorMessage("Deck not found.", ResultType.NOT_FOUND);
            return result;
        }
        List<CardDeck> cardDeckList = cardDeckRepository.fetchAllCardDecksFromDeckId(deckId);
        for(CardDeck cardDeck : cardDeckList){
            Card card = cardRepository.fetchCardById(cardDeck.cardId());
            cardList.add(card);
        }
        deck.setCardList(cardList);
        result.setpayload(deck);
        return result;
    }

    public Result<Deck> createDeckInCollection(Deck deck, int collectionId){
        Result<Deck> result = new Result<>();
        if(deck == null){
            result.addErrorMessage("Deck cannot be null.",ResultType.INVALID);
            return result;
        }
        validate(result,deck);
        if(!result.isSuccess()){
            return result;
        }
        if(collectionRepository.fetchCollectionByCollectionId(collectionId) == null){
            result.addErrorMessage("Collection not found.", ResultType.NOT_FOUND);
            return result;
        }
        Deck createdDeck = deckRepository.createDeck(deck);
        if(createdDeck == null){
            result.addErrorMessage("Error creating deck.", ResultType.INVALID);
            return result;
        }
        CollectionDeck collectionDeck = collectionDeckRepository.createCollectionDeck(createdDeck.getDeckId(),collectionId);
        if(collectionDeck == null){
            result.addErrorMessage("Error adding deck to collection.", ResultType.INVALID);
            return result;
        }
        result.setpayload(createdDeck);
        return result;
    }

    private void validate(Result<Deck> result, Deck deck){
        // Validate using validators first
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Deck>> violations = validator.validate(deck);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Deck> violation : violations) {
                result.addErrorMessage(violation.getMessage(), ResultType.INVALID);
            }
        }
    }
}
