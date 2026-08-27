package mtgcollection.data.jdbc;

import mtgcollection.data.interfaces.CollectionDeckRepository;
import mtgcollection.model.CollectionDeck;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionDeckJdbcRepository implements CollectionDeckRepository {

    private final JdbcClient jdbcClient;

    public CollectionDeckJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public CollectionDeck createCollectionDeck(int deckId, int collectionId) {
        final String sql = """
                insert into collection_deck (collection_id, deck_id)
                values(:collectionId, :deckId);
                """;
        boolean isCreated = jdbcClient.sql(sql)
                .param("collectionId", collectionId)
                .param("deckId", deckId)
                .update() == 1;
        if(isCreated){
            return new CollectionDeck(deckId,collectionId);
        }else{
            return null;
        }
    }
}
