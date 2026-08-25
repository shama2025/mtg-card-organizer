package mtgcollection.data;

import mtgcollection.model.Card;
import mtgcollection.model.CardCollection;
import mtgcollection.model.Collection;

public interface CardCollectionRepository {

    CardCollection addCardToCollection(Card cardToAdd, Collection collection);
}
