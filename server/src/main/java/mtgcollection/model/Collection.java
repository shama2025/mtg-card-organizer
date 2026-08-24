package mtgcollection.model;

import java.util.List;
import java.util.Objects;

public class Collection {
    private int collectionId;
    private List<Card> cardList;
    private int ownerId;

    public Collection(int collectionId, List<Card> cardList, int ownerId) {
        this.collectionId = collectionId;
        this.cardList = cardList;
        this.ownerId = ownerId;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public int getOwner() {
        return ownerId;
    }

    public void setOwner(int ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return collectionId == that.collectionId && Objects.equals(cardList, that.cardList) && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectionId, cardList, ownerId);
    }
}
