package mtgcollection.data;

import mtgcollection.model.Collection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionJdbcRepository implements CollectionRepository{

    private JdbcClient jdbcClient;

    public CollectionJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Collection createCollection(int userId){
        final String sql = """
                insert into collection (user_id)
                values(:userId);
                """;
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .param("userId", userId)
                .update(keyHolder,"collection_id");
        return new Collection(keyHolder.getKey().intValue(),userId);
    }
}
