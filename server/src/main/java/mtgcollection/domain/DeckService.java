package mtgcollection.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import mtgcollection.data.http.ScryFallApiHttpRepository;
import mtgcollection.data.http.response.model.CardResponse;
import mtgcollection.data.interfaces.*;
import mtgcollection.model.*;
import mtgcollection.model.card.Card;
import mtgcollection.model.card.ManaColor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DeckService {

    private final DeckRepository deckRepository;

    private final CardDeckRepository cardDeckRepository;

    private final CardRepository cardRepository;

    private final CollectionRepository collectionRepository;

    private final CollectionDeckRepository collectionDeckRepository;

    private final ScryFallApiHttpRepository scryFallApiHttpRepository;


    public DeckService(DeckRepository deckRepository,CardDeckRepository cardDeckRepository,
                       CardRepository cardRepository, CollectionRepository collectionRepository,
                       CollectionDeckRepository collectionDeckRepository,
                       ScryFallApiHttpRepository scryFallApiHttpRepository) {
        this.deckRepository = deckRepository;
        this.cardDeckRepository = cardDeckRepository;
        this.cardRepository = cardRepository;
        this.collectionRepository = collectionRepository;
        this.collectionDeckRepository = collectionDeckRepository;
        this.scryFallApiHttpRepository = scryFallApiHttpRepository;
    }

    public List<Deck> fetchAllDecksByCollectionId(int collectionId){return deckRepository.fetchAllDecksInACollection(collectionId);}

    public Result<Deck> fetchDeckByDeckId(int deckId){
        Result<Deck> result = new Result<>();
        List<Card> cardList = new ArrayList<>();
        Deck deck;
        try{
            deck = deckRepository.fetchDeckByDeckId(deckId);
            if(deck == null){
                result.addErrorMessage("Deck not found.", ResultType.NOT_FOUND);
                return result;
            }
        }catch(EmptyResultDataAccessException ex){
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
        try{
            if(collectionRepository.fetchCollectionByCollectionId(collectionId) == null){
                result.addErrorMessage("Collection not found.", ResultType.NOT_FOUND);
                return result;
            }
        }catch (EmptyResultDataAccessException ex){
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

    public Result<Deck> updateDeck(Deck deck){
        Result<Deck> result = new Result<>();
        validate(result,deck);
        if(!result.isSuccess()){
            return result;
        }
        try{
            deckRepository.fetchDeckByDeckId(deck.getDeckId());
        }catch(EmptyResultDataAccessException ex){
            result.addErrorMessage("Deck does not exist.",ResultType.NOT_FOUND);
            return result;
        }
        boolean isUpdated = deckRepository.updateDeck(deck);
        if(!isUpdated){
            result.addErrorMessage("Error updating deck.", ResultType.INVALID);
        }else{
            result.setpayload(deck);
        }
        return result;
    }

    public Result<Deck> addCardToDeck(Deck deck, String cardName) throws InterruptedException, JsonProcessingException {
        Result<Deck> result = new Result<>();
        validate(result,deck);
        if(!result.isSuccess()){
            return result;
        }
        try{
            deckRepository.fetchDeckByDeckId(deck.getDeckId());
        }catch(EmptyResultDataAccessException ex){
            result.addErrorMessage("Deck does not exist.",ResultType.NOT_FOUND);
            return result;
        }

        Card card;
        try {
            card = fetchCardJdbcRepo(cardName);
            if (card == null) {
                result.addErrorMessage("Card not found.", ResultType.NOT_FOUND);
                return result;
            }
            } catch (EmptyResultDataAccessException ex) {
            card = fetchCardHttpRepo(cardName);
            if (card == null) {
                result.addErrorMessage("Card not found.", ResultType.NOT_FOUND);
                return result;
            }

            card = cardRepository.addCard(card);
        }
        // Add card to card_deck table
        CardDeck cardDeck = cardDeckRepository.addCardDeck(card.getId(),deck.getDeckId());
        if(cardDeck == null){
            result.addErrorMessage("Error adding card to deck.",ResultType.INVALID);
            return result;
        }
        // Update Deck
        boolean isDeckUpdated = deckRepository.updateDeck(deck);
        if(isDeckUpdated){

            result.setpayload(deck);
        }else{
            result.addErrorMessage("Error updating deck.",ResultType.INVALID);
        }
        return result;
    }

    public Result<Integer> updateCardInADeck( int cardId,int deckId, int quantity){
        Result<Integer> result = new Result<>();

        if(quantity < 0){
            result.addErrorMessage("Quantity cannot be below 0.",ResultType.INVALID);
            return result;
        }

        try{
            cardRepository.fetchCardById(cardId);
        }catch(EmptyResultDataAccessException ex){
            result.addErrorMessage("Card does not exist.",ResultType.NOT_FOUND);
            return result;
        }

        try{
            deckRepository.fetchDeckByDeckId(deckId);
        }catch(EmptyResultDataAccessException ex){
            result.addErrorMessage("Deck does not exist.",ResultType.NOT_FOUND);
            return result;
        }

        boolean isUpdated = cardDeckRepository.updateCardInDeck(cardId,deckId,quantity);
        if(isUpdated){
            Deck deck = deckRepository.fetchDeckByDeckId(deckId);
            deck.setDateUpdated(LocalDate.now());
            boolean isDeckUpdate = deckRepository.updateDeck(deck);
            if(isDeckUpdate){
                result.setpayload(deckId);
            }
        }else{
            result.addErrorMessage("Error updating card in a deck.", ResultType.INVALID);
        }
        return result;
    }

    public Result<Integer> removeCardFromDeck(int cardId, int deckId){
        Result<Integer> result = new Result<>();

        try{
            cardRepository.fetchCardById(cardId);
        }catch(EmptyResultDataAccessException ex){
            result.addErrorMessage("Card does not exist.",ResultType.NOT_FOUND);
            return result;
        }

        try{
            deckRepository.fetchDeckByDeckId(deckId);
        }catch(EmptyResultDataAccessException ex){
            result.addErrorMessage("Deck does not exist.",ResultType.NOT_FOUND);
            return result;
        }

        boolean isRemoved = cardDeckRepository.removeCardFromDeck(cardId,deckId);
        if(isRemoved){
            Deck deck = deckRepository.fetchDeckByDeckId(deckId);
            deck.setDateUpdated(LocalDate.now());
            boolean isDeckUpdate = deckRepository.updateDeck(deck);
            if(isDeckUpdate){
                result.setpayload(deckId);
            }
        }else{
            result.addErrorMessage("Error updating card in a deck.", ResultType.INVALID);
        }
        return result;
    }

    public Result<Integer> removeDeck(int deckId){
        Result<Integer> result = new Result<>();
        try{
            Deck deck = deckRepository.fetchDeckByDeckId(deckId);
            if(deck == null){
                result.addErrorMessage("Deck not found.", ResultType.NOT_FOUND);
                return result;
            }
        }catch(EmptyResultDataAccessException ex){
            result.addErrorMessage("Deck not found.", ResultType.NOT_FOUND);
            return result;
        }

        boolean isCardDeckRemoved = cardDeckRepository.removeDeck(deckId);
        boolean isCollectionDeckRemoved = collectionDeckRepository.removeDeck(deckId);
        boolean isDeckRemoved = deckRepository.removeDeck(deckId);

        if(isCardDeckRemoved && isDeckRemoved && isCollectionDeckRemoved){
            result.setpayload(deckId);
            return result;
        }
        result.addErrorMessage("Error deleting deck.",ResultType.INVALID);
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

    private Card parseCardResponse(Optional<CardResponse> cardResponse){
        if(cardResponse.isEmpty()){return null;} // Card not found

        Card card = new Card();
        card.setId(0);
        card.setCardId(cardResponse.get().cardId());
        card.setName(cardResponse.get().name().toUpperCase());
        card.setArtistName(cardResponse.get().artist());
        card.setQuantity(0);

        card.parseSet(cardResponse.get().set(),cardResponse.get().setName());
        card.parseLegalities(cardResponse.get().legalities());
        card.setImgPath(List.of(cardResponse.get().imageUris()));
        card.setManaColor(new ManaColor(cardResponse.get().colors()));
        card.parseCardManaCost(cardResponse.get().manaCost());

        return card;
    }

    private Card fetchCardJdbcRepo(String cardName){
        Card card = cardRepository.fetchCardByName(cardName.toUpperCase());
        return card;
    }

    private Card fetchCardHttpRepo(String cardName) throws InterruptedException {
        return parseCardResponse(scryFallApiHttpRepository.fetchCardFromScryfallByName(cardName));
    }
}
