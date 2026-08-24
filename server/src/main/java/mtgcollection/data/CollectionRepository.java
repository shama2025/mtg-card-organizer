package mtgcollection.data;

import mtgcollection.model.Card;

import java.util.List;

public interface CollectionRepository {

    List<Card> fetchAllCards(int userId);

}
