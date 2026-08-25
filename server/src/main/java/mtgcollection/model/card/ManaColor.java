package mtgcollection.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ManaColor(String []colors) {
    @JsonCreator
    public static ManaColor fromList(String [] colors) {
        return new ManaColor(colors);
    }
}
