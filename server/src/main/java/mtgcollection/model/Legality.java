package mtgcollection.model;

import java.util.Objects;

public class Legality {
    private int legalityId;
    private String format;

    public Legality(int legalityId, String format) {
        this.legalityId = legalityId;
        this.format = format;
    }

    public int getLegalityId() {
        return legalityId;
    }

    public void setLegalityId(int legalityId) {
        this.legalityId = legalityId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Legality legality = (Legality) o;
        return legalityId == legality.legalityId && Objects.equals(format, legality.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(legalityId, format);
    }
}
