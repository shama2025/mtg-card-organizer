package mtgcollection.data.jdbc;

import mtgcollection.data.interfaces.CollectionDeckRepository;
import mtgcollection.model.CollectionDeck;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CollectionDeckJdbcRepository implements CollectionDeckRepository {

    private final JdbcClient jdbcClient;

    public CollectionDeckJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public CollectionDeck fetchCollectionDeckByDeckId(int deckId) {
        final String sql = """
                select cd.collection_id, cd.deck_id
                from collection_deck cd
                where cd.deck_id = :deckId
                """;
        return jdbcClient.sql(sql)
                .param("deckId", deckId)
                .query(CollectionDeck.class)
                .single();
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

    @Transactional
    @Override
    public boolean removeDeck(int deckId) {
        final String sql = """
                delete from collection_deck where deck_id = :deckId;
                """;
        return jdbcClient.sql(sql)
                .param("deckId",deckId)
                .update() == 1;
    }
}
