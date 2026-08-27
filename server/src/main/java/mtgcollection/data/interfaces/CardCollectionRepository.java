package mtgcollection.data.interfaces;

import mtgcollection.model.card.Card;
import mtgcollection.model.CardCollection;
import mtgcollection.model.Collection;

public interface CardCollectionRepository {

    CardCollection addCardToCollection(int cardToAddId, int collectionId);

    boolean updateCardInCollection(int cardId, int collectionId,int quantity);

    boolean removeCardFromCollection(int cardId, int collectionId);
}
