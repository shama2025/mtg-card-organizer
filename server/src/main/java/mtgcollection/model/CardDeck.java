package mtgcollection.model;

import mtgcollection.model.card.Card;

public record CardDeck(
        Card card,
        Deck deck,
        int quantity) {
}
