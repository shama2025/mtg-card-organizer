package mtgcollection.model.card;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ManaCost(
        @JsonProperty("cmc")
        int cmc,
        @JsonProperty("mana_string")
        String manaString) {

    public List<String> extractSymbols() {
        if (manaString == null || manaString.isBlank()) {
            return List.of();
        }
        List<String> symbols = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\{([^}]+)}").matcher(manaString);
        while (matcher.find()) {
            symbols.add(matcher.group(1));
        }
        return symbols;
    }
}
