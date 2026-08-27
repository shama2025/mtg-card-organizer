package mtgcollection.data.jdbc.mapper;

import mtgcollection.model.Deck;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DeckMapper implements RowMapper<Deck> {
    @Override
    public Deck mapRow(ResultSet rs, int rowNum) throws SQLException {

        int cardCount;
        try{
            cardCount = rs.getObject("card_count", Integer.class);
        }catch(NullPointerException ex){
            cardCount = 0;
        }

        return new Deck(
                rs.getInt("deck_id"),
                rs.getString("name"),
                cardCount,
                rs.getObject("date_created", LocalDate.class),
                rs.getObject("date_updated", LocalDate.class),
                null
        );
    }
}
