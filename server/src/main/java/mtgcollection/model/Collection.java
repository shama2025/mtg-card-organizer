package mtgcollection.model;

import java.util.Objects;

public class Collection {
    private int collectionId;
    private int ownerId;

    public Collection(int collectionId, int ownerId) {
        this.collectionId = collectionId;
        this.ownerId = ownerId;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
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
        return collectionId == that.collectionId && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectionId, ownerId);
    }
}
