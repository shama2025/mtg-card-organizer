package mtgcollection.data;

import mtgcollection.model.Card;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardJdbcRepository implements CardRepository {

    private JdbcClient jdbcClient;

    public CardJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Card> fetchAllCards(int userId){
        final String sql = """
                select card.card_id ,card.card_uuid, card.name, card.img_path,
                card.mana_color, card.mana_cost, card.sets, card.legalities, card.artist as artistName, quantity from card
                inner join collection on collection.card_id = card.card_id
                inner join `user` on `user`.user_id = collection.user_id
                where `user`.user_id = :userId
                """;
        return jdbcClient.sql(sql)
                .param("userId", userId)
                .query(Card.class)
                .list();
    }
}
