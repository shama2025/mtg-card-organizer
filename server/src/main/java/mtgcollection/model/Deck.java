package mtgcollection.model;

import jakarta.validation.constraints.*;
import mtgcollection.model.card.Card;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Deck {
    private int id;

    @NotNull(message = "Name cannot be null.")
    @NotBlank(message = "Name cannot be blank.")
    private String name;
    private int cardCount;

    @NotNull(message = "Creation date cannot be null.")
    @PastOrPresent(message = "Creation date has to be today.")
    @FutureOrPresent(message = "Creation date has to be today.")
    private LocalDate dateCreated;

    private LocalDate dateUpdated;
    private List<Card> cardList = new ArrayList<>();

    public Deck(int id, String name, int cardCount, LocalDate dateCreated, LocalDate dateUpdated, List<Card> cardList) {
        this.id = id;
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
        return id;
    }

    public void setDeckId(int id) {
        this.id = id;
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
        return id == deck.id && cardCount == deck.cardCount && Objects.equals(name, deck.name) && Objects.equals(dateCreated, deck.dateCreated) && Objects.equals(dateUpdated, deck.dateUpdated) && Objects.equals(cardList, deck.cardList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cardCount, dateCreated, dateUpdated, cardList);
    }
}
