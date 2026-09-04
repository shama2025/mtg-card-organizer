package mtgcollection.data.interfaces;

import mtgcollection.model.Collection;

public interface CollectionRepository {

    Collection fetchCollectionByCollectionId(int collectionId);

    Collection fetchUserCollection(int userId);

    Collection createCollection(int userId);

}
