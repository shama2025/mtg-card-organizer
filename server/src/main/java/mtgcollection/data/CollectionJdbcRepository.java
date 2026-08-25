package mtgcollection.data;

import mtgcollection.model.Collection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class CollectionJdbcRepository implements CollectionRepository{

    private JdbcClient jdbcClient;

    public CollectionJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Collection createCollection(int userId){
        final String sql = """
                insert into (card_id, user_id)
                values(0, :userId);
                """;
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .param("userId", userId)
                .update(keyHolder,"collection_id");
        return new Collection(keyHolder.getKey().intValue(),null,userId);
    }

    @Override
    public boolean addCardToCollection(int cardId, int userId) {
        final String sql = """
                update collection
                set card_id = :cardId
                where user_id = :userId;
                """;

        return jdbcClient.sql(sql)
                .param("cardId", cardId)
                .param("userId", userId)
                .update() > 0;
    }
}
