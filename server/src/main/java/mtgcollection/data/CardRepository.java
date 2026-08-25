package mtgcollection.data;

import mtgcollection.model.Card;

import java.util.List;

public interface CardRepository {

    List<Card> fetchAllCards(int collectionId);

    Card addCard(Card card);
}
