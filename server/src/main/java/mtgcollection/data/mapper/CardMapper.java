package mtgcollection.data.mapper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mtgcollection.model.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CardMapper implements RowMapper<Card> {
    @Override
    public Card mapRow(ResultSet rs, int rowNum) throws SQLException {
        Card card = new Card();
        ObjectMapper mapper = new ObjectMapper();

        String legalities = rs.getString("card.legalities");
        if(legalities != null && !legalities.isBlank()){
            // Format it
            try{
                card.setLegalities(mapper.readValue(legalities, new TypeReference<List<Formats>>() {}));
            }catch (JacksonException ex){
                throw new SQLException("Error parsing mana cost column",ex);
            }
        }

        String sets = rs.getString("card.sets");
        if(sets != null && !sets.isBlank()){
            // Format it
            try{
                card.setSet(mapper.readValue(sets, Set.class));
            }catch (JacksonException ex){
                throw new SQLException("Failed to parse sets JSON column", ex);
            }
        }

        String manaColor = rs.getString("card.mana_color");
        if(manaColor != null && !manaColor.isBlank()){
            // Format it
            try{
                card.setManaColor(mapper.readValue(manaColor, new TypeReference<ManaColor>() {}));
            }catch (JacksonException ex){
                throw new SQLException("Error parsing mana color column",ex);
            }
        }

        String manaCost = rs.getString("card.mana_cost");
        if(manaCost != null && !manaCost.isBlank()){
            // Format it
            try{
                card.setManaCost(mapper.readValue(manaCost, ManaCost.class));
            }catch (JacksonException ex){
                throw new SQLException("Error parsing mana cost column",ex);
            }
        }

        card.setCardId(UUID.fromString(rs.getString("card.card_uuid")));
        card.setId(rs.getInt("card.card_id"));
        card.setName(rs.getString("card.name"));
        card.setQuantity(rs.getInt("collection_card.quantity"));
        card.setImgPath(rs.getString("card.img_path"));
        card.setArtistName(rs.getString("card.artist"));

        return card;
    }
}
