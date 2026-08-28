package mtgcollection.data.interfaces;

import mtgcollection.model.CardDeck;

import java.util.List;

public interface CardDeckRepository {

    List<CardDeck> fetchAllCardDecksFromDeckId(int deckId);

    CardDeck addCardDeck(int cardId, int deckId);

    boolean removeDeck(int deckId);

}
