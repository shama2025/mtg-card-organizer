package mtgcollection.data.interfaces;

import mtgcollection.model.Deck;

import java.util.List;

public interface DeckRepository {

    List<Deck> fetchAllDecksInACollection(int collectionId);
}
