package mtgcollection.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Card {
    private int cardId;
    private UUID cardUuid;
    private String name;
    private List<Formats> legalities;
    private Set sets;
    private String imgPath;
    private ManaColor manaColor;
    private ManaCost manaCost;
    private String artistName;
    private int quantity;

    public Card(){}

    public Card(int cardId,UUID cardUuid, String name, Set sets, List<Formats> legalities, String imgPath, ManaColor manaColor, ManaCost manaCost, String artistName, int quantity){
        this.cardId = cardId;
        this.cardUuid = cardUuid;
        this.name = name;
        this.sets = sets;
        this.legalities = legalities;
        this.imgPath = imgPath;
        this.manaColor = manaColor;
        this.manaCost = manaCost;
        this.artistName = artistName;
        this.quantity = quantity;
    }

    public int getId() {
        return cardId;
    }

    public void setId(int id) {
            this.cardId = id;
    }

    public UUID getCardId() {
        return cardUuid;
    }

    public void setCardId(UUID cardId) {
        this.cardUuid = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Formats> getLegalities() {
        return legalities;
    }

    public void setLegalities(List<Formats> legalities) {
        this.legalities = legalities;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Set getSet() {
        return sets;
    }

    public void setSet(Set sets) {
        this.sets = sets;
    }

    public ManaColor getManaColor() {
        return manaColor;
    }

    public void setManaColor(ManaColor manaColor) {
        this.manaColor = manaColor;
    }

    public ManaCost getManaCost() {
        return manaCost;
    }

    public void setManaCost(ManaCost manaCost) {
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
        return cardId == card.cardId && quantity == card.quantity && Objects.equals(cardUuid, card.cardUuid) && Objects.equals(name, card.name) && Objects.equals(legalities, card.legalities) && Objects.equals(sets, card.sets) && Objects.equals(imgPath, card.imgPath) && Objects.equals(manaColor, card.manaColor) && Objects.equals(manaCost, card.manaCost) && Objects.equals(artistName, card.artistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, cardUuid, name, legalities, sets, imgPath, manaColor, manaCost, artistName, quantity);
    }
}
