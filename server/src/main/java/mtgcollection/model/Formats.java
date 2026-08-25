package mtgcollection.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Format;

public enum Formats {
    STANDARD("standard"),
    FUTURE("future"),
    HISTORIC("historic"),
    TIMETABLE("timeless"),
    GLADIATOR("gladiator"),
    PIONEER("pioneer"),
    EXPLORER("explorer"),
    MODERN("modern"),
    LEGACY("legacy"),
    PAUPER("pauper"),
    VINTAGE("vintage"),
    PENNY("penny"),
    COMMANDER("commander"),
    OATHBREAKER("oathbreaker"),
    BRAWL("brawl"),
    ALCHEMY("alchemy"),
    PAUPERCOMMANDER("paupercommander"),
    DUEL("duel"),
    OLDSCHOOL("oldschool"),
    PREMODERN("premodern"),
    PREDH("predh");

    private String value;

    Formats(String  format) {
        this.value = format;
    }

    public void setValue (String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
