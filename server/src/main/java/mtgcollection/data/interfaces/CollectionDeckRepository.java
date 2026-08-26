package mtgcollection.data.interfaces;

import mtgcollection.model.CollectionDeck;

public interface CollectionDeckRepository {

    CollectionDeck createCollectionDeck(int deckId, int collectionId);
}
