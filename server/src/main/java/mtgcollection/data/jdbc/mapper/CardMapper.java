package mtgcollection.data.jdbc.mapper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mtgcollection.model.card.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CardMapper implements RowMapper<Card> {
    @Override
    public Card mapRow(ResultSet rs, int rowNum) throws SQLException {
            Card card = new Card();
            ObjectMapper mapper = new ObjectMapper();

            card.setId(rs.getInt("card_id"));
            card.setName(rs.getString("name"));
            card.setArtistName(rs.getString("artist"));
            card.setQuantity(rs.getInt("quantity"));

            String uuidStr = rs.getString("card_uuid");
            if (uuidStr != null && !uuidStr.isBlank()) {
                card.setCardId(UUID.fromString(uuidStr));
            }

            String legalities = rs.getString("legalities");
            if (legalities != null && !legalities.isBlank()) {
                try {
                    card.setLegalities(mapper.readValue(legalities, new TypeReference<List<String>>() {}));
                } catch (JacksonException ex) {
                    throw new SQLException("Error parsing legalities JSON column", ex);
                }
            }

            String sets = rs.getString("sets");
            if (sets != null && !sets.isBlank()) {
                try {
                    card.setSet(mapper.readValue(sets, Set.class));
                } catch (JacksonException ex) {
                    throw new SQLException("Error parsing sets JSON column", ex);
                }
            }

            String manaColor = rs.getString("mana_color");
            if (manaColor != null && !manaColor.isBlank()) {
                try {
                    card.setManaColor(mapper.readValue(manaColor, ManaColor.class));
                } catch (JacksonException ex) {
                    throw new SQLException("Error parsing mana color JSON column", ex);
                }
            }

            String manaCost = rs.getString("mana_cost");
            if (manaCost != null && !manaCost.isBlank()) {
                try {
                    card.setManaCost(mapper.readValue(manaCost, ManaCost.class));
                } catch (JacksonException ex) {
                    throw new SQLException("Error parsing mana cost JSON column", ex);
                }
            }

            String imageUri = rs.getString("img_path");
            if (imageUri != null && !imageUri.isBlank()) {
                try {
                    card.setImgPath(mapper.readValue(imageUri, new TypeReference<List<Map<String, String>>>() {}));
                } catch (JacksonException ex) {
                    throw new SQLException("Error parsing image path JSON column", ex);
                }
            }

            return card;
        }
}
