package mtgcollection.model;

import java.util.Objects;

public class Set {
    private int setId;
    private String code;
    private String name;

    public Set(int setId, String code, String name) {
        this.setId = setId;
        this.code = code;
        this.name = name;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Set set = (Set) o;
        return setId == set.setId && Objects.equals(code, set.code) && Objects.equals(name, set.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(setId, code, name);
    }
}
