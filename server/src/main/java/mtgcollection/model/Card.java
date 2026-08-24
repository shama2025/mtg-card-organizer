package mtgcollection.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Card {
    private int id;
    private UUID cardId;
    private String name;
    private List<Legality> legalities;
    private Set set;
    private String imgPath;
    private String manaColor;
    private String manaCost;
    private String artistName;
    private int quantity;

    public Card(int id,UUID cardId, String name, Set set, List<Legality> legalities, String imgPath, String manaColor, String manaCost, String artistName, int quantity) {
        this.id = id;
        this.cardId = cardId;
        this.name = name;
        this.set = set;
        this.legalities = legalities;
        this.imgPath = imgPath;
        this.manaColor = manaColor;
        this.manaCost = manaCost;
        this.artistName = artistName;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getCardId() {
        return cardId;
    }

    public void setCardId(UUID cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Legality> getLegalities() {
        return legalities;
    }

    public void setLegalities(List<Legality> legalities) {
        this.legalities = legalities;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public String getManaColor() {
        return manaColor;
    }

    public void setManaColor(String manaColor) {
        this.manaColor = manaColor;
    }

    public String getManaCost() {
        return manaCost;
    }

    public void setManaCost(String manaCost) {
        this.manaCost = manaCost;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && quantity == card.quantity && Objects.equals(cardId, card.cardId) && Objects.equals(name, card.name) && Objects.equals(legalities, card.legalities) && Objects.equals(set, card.set) && Objects.equals(imgPath, card.imgPath) && Objects.equals(manaColor, card.manaColor) && Objects.equals(manaCost, card.manaCost) && Objects.equals(artistName, card.artistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardId, name, legalities, set, imgPath, manaColor, manaCost, artistName, quantity);
    }
}
