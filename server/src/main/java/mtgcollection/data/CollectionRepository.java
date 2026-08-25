package mtgcollection.data;

import mtgcollection.model.Collection;

public interface CollectionRepository {

    Collection createCollection(int userId);

    boolean addCardToCollection(int cardId, int userId);

}
