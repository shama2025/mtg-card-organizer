package mtgcollection.data.jdbc.mapper;

import mtgcollection.model.Deck;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DeckMapper implements RowMapper<Deck> {
    @Override
    public Deck mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Deck(
                rs.getInt("deck_id"),
                rs.getString("name"),
                rs.getObject("card_count", Integer.class),
                rs.getObject("date_created", LocalDate.class),
                rs.getObject("date_updated", LocalDate.class)
        );
    }
}
