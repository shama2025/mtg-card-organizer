package mtgcollection.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.Null;
import mtgcollection.data.http.ScryFallApiHttpRepository;
import mtgcollection.data.http.response.model.CardResponse;
import mtgcollection.data.interfaces.CardCollectionRepository;
import mtgcollection.data.interfaces.CardRepository;
import mtgcollection.data.interfaces.CollectionRepository;
import mtgcollection.model.*;
import mtgcollection.model.card.Card;
import mtgcollection.model.CardCollection;
import mtgcollection.model.Collection;
import mtgcollection.model.card.ManaColor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CollectionService {

    private CardRepository cardRepository;
    private CollectionRepository collectionRepository;
    private CardCollectionRepository cardCollectionRepository;
    private ScryFallApiHttpRepository scryFallApiHttpRepository;

    public CollectionService(CardRepository cardRepository,
                             CollectionRepository collectionRepository,
                             CardCollectionRepository cardCollectionRepository,
                             ScryFallApiHttpRepository scryFallApiHttpRepository) {
        this.cardRepository = cardRepository;
        this.collectionRepository = collectionRepository;
        this.cardCollectionRepository = cardCollectionRepository;
        this.scryFallApiHttpRepository = scryFallApiHttpRepository;
    }

    public Collection findCollectionByUserId(int userId){return collectionRepository.fetchUserCollection(userId);}

    public List<Card> fetchAllCardsByCollection(int collectionId){
        return cardRepository.fetchAllCards(collectionId);
    }

    public Result<Card> addCardToCollection(String cardName, int collectionId) throws InterruptedException, JsonProcessingException {
        Result<Card> result = new Result<>();
        validate(result,cardName,collectionId);
        if(!result.isSuccess()){
            return result;
        }
        // Check if card is in db
        Card card;
        try{
            // Card already in db
            card = fetchCardJdbcRepo(cardName);
        }catch(EmptyResultDataAccessException ex){
            // card not found
            card = fetchCardHttpRepo(cardName);
            if(card == null){
                result.addErrorMessage("Card not found.", ResultType.NOT_FOUND);
                return result;
            }
        }
        card = new Card(0, card.getCardId(),card.getName(),
                card.getSet(),card.getLegalities(), card.getImgPath(),
                card.getManaColor(),card.getManaCost(),
                card.getArtistName(),1);

        card = cardRepository.addCard(card);
        CardCollection cardCollection = cardCollectionRepository.addCardToCollection(card.getId(),collectionId);
        if(cardCollection.collectionId() != collectionId
                && cardCollection.cardId() != card.getId()){
            result.addErrorMessage("Error adding card to collection", ResultType.INVALID);
        }
        result.setpayload(card);
        return result;
    }

    public Result<Integer> updateCardInCollection(int cardId, int collectionId, int quantity){
        Result<Integer> result = new Result<>();
        validate(result,cardId,collectionId);
        if(!result.isSuccess()){
            return result;
        }
        if(quantity < 0){
            result.addErrorMessage("Quantity cannot be below 0.", ResultType.INVALID);
            return result;
        }
        // Add check for if quantity is 0??
        boolean isUpdated = cardCollectionRepository.updateCardInCollection(cardId,collectionId,quantity);
        if(!isUpdated){
            result.addErrorMessage("Error updating card quantity.", ResultType.INVALID);
            return result;
        }
        result.setpayload(cardId);
        return result;
    }

    public Result<Integer> removeCardFromCollection(int cardId, int collectionId){
        Result<Integer> result = new Result<>();
        validate(result,cardId,collectionId);
        if(!result.isSuccess()){
            return result;
        }
        boolean isRemoved = cardCollectionRepository.removeCardFromCollection(cardId,collectionId);
        if(!isRemoved){
            result.addErrorMessage("Error removing card from collection.", ResultType.INVALID);
            return result;
        }
        result.setpayload(cardId);
        return result;
    }

    private void validate(Result<Card> result, String cardName, int collectionId){
        if(cardName == null || cardName.isBlank()){
            result.addErrorMessage("Card name cannot be empty.", ResultType.INVALID);
        }
        try{
            if(collectionRepository.fetchCollectionByCollectionId(collectionId) == null){
                result.addErrorMessage("Collection was not found.", ResultType.NOT_FOUND);
            }
        }catch (EmptyResultDataAccessException ex){
            result.addErrorMessage("Collection was not found.", ResultType.NOT_FOUND);
        }
    }

    private void validate(Result<Integer> result, int cardId, int collectionId){
        try{
            if(cardRepository.fetchCardById(cardId) == null){
                result.addErrorMessage("Card was not found.", ResultType.NOT_FOUND);
            }
        }catch (EmptyResultDataAccessException ex){
            result.addErrorMessage("Card was not found.", ResultType.NOT_FOUND);
        }
        try{
            Collection collection = collectionRepository.fetchCollectionByCollectionId(collectionId);
            if(collection == null){
                result.addErrorMessage("Collection was not found.", ResultType.NOT_FOUND);
            }
        }catch (EmptyResultDataAccessException ex){
            result.addErrorMessage("Collection was not found.", ResultType.NOT_FOUND);
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
