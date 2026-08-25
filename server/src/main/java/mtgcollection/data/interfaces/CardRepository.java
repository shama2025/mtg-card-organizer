package mtgcollection.data.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import mtgcollection.model.card.Card;

import java.util.List;

public interface CardRepository {

    Card fetchCardByName(String cardName);

    List<Card> fetchAllCards(int collectionId);

    Card addCard(Card card) throws JsonProcessingException;
}
