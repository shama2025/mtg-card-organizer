package mtgcollection.data.interfaces;

import mtgcollection.model.CollectionDeck;

public interface CollectionDeckRepository {

    CollectionDeck fetchCollectionDeckByDeckId(int deckId);

    CollectionDeck createCollectionDeck(int deckId, int collectionId);

    boolean removeDeck(int deckId);
}
