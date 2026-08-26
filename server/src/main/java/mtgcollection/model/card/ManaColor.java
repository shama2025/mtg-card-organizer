package mtgcollection.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;

public record ManaColor(String []colors) {
    @JsonCreator
    public static ManaColor fromList(String [] colors) {
        return new ManaColor(colors);
    }
}
