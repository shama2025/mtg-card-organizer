package mtgcollection.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public record ManaColor(List<String> colors) {
    @JsonCreator
    public static ManaColor fromList(List<String> colors) {
        return new ManaColor(colors);
    }
}
