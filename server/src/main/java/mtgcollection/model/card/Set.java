package mtgcollection.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Set(
        @JsonProperty("code")
        String code,
        @JsonProperty("name")
        String name) {
}
