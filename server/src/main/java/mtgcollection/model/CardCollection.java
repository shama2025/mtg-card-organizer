package mtgcollection.model;

import mtgcollection.model.card.Card;

public record CardCollection(Card card, Collection collection, int quantity) {
}
