package mtgcollection.model;

import jakarta.validation.constraints.*;
import mtgcollection.model.card.Card;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Deck {
    private int deckId;

    @NotNull(message = "Name cannot be null.")
    @NotBlank(message = "Name cannot be blank.")
    private String name;
    private int cardCount;

    @NotNull(message = "Creation date cannot be null.")
    @PastOrPresent(message = "Creation date has to be today.")
    @FutureOrPresent(message = "Creation date has to be today.")
    private LocalDate dateCreated;

    @NotNull(message = "Updated date cannot be null.")
    @FutureOrPresent(message = "Updated date has to be today or in future.")
    private LocalDate dateUpdated;
    private List<Card> cardList;

    public Deck(int deckId, String name, int cardCount, LocalDate dateCreated, LocalDate dateUpdated, List<Card> cardList) {
        this.deckId = deckId;
        this.name = name;
        this.cardCount = cardCount;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.cardList = cardList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
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

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return deckId == deck.deckId && cardCount == deck.cardCount && Objects.equals(name, deck.name) && Objects.equals(dateCreated, deck.dateCreated) && Objects.equals(dateUpdated, deck.dateUpdated) && Objects.equals(cardList, deck.cardList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deckId, name, cardCount, dateCreated, dateUpdated, cardList);
    }
}
