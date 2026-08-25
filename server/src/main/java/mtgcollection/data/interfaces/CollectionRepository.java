package mtgcollection.data.interfaces;

import mtgcollection.model.Collection;

public interface CollectionRepository {

    Collection createCollection(int userId);

}
