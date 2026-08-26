package mtgcollection.model;

import java.time.LocalDate;
import java.util.Objects;

public class Deck {
    private int deckId;
    private String name;
    private int cardCount;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;

    public Deck(int deckId, String name, int cardCount, LocalDate dateCreated, LocalDate dateUpdated) {
        this.deckId = deckId;
        this.name = name;
        this.cardCount = cardCount;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDate dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return deckId == deck.deckId && cardCount == deck.cardCount && Objects.equals(name, deck.name) && Objects.equals(dateCreated, deck.dateCreated) && Objects.equals(dateUpdated, deck.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deckId, name, cardCount, dateCreated, dateUpdated);
    }
}
