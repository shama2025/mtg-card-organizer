package mtgcollection.data.interfaces;

import mtgcollection.model.Collection;

public interface CollectionRepository {

    Collection fetchUserCollection(int userId);

    Collection createCollection(int userId);

}
