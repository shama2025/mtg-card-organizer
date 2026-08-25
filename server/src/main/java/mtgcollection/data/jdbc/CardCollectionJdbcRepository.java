package mtgcollection.data.jdbc;

import mtgcollection.data.interfaces.CardCollectionRepository;
import mtgcollection.model.Card;
import mtgcollection.model.CardCollection;
import mtgcollection.model.Collection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class CardCollectionJdbcRepository implements CardCollectionRepository {

    private JdbcClient jdbcClient;

    public CardCollectionJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public CardCollection addCardToCollection(Card cardToAdd, Collection collection) {
        final String sql = """
                insert into collection_card(collection_id,card_id,quantity)
                values(:collectionId, :cardId, 1);
                """;

        jdbcClient.sql(sql)
                .param("cardId", cardToAdd.getId())
                .param("collectionId", collection.getCollectionId())
                .update();
        return new CardCollection(cardToAdd,collection,1);
    }
}
