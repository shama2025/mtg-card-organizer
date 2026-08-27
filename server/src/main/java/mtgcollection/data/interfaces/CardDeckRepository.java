package mtgcollection.data.interfaces;

import mtgcollection.model.CardDeck;

import java.util.List;

public interface CardDeckRepository {

    List<CardDeck> fetchAllCardDecksFromDeckId(int deckId);

    boolean removeDeck(int deckId);

}
