package mtgcollection.data.jdbc;

import mtgcollection.data.interfaces.CardCollectionRepository;
import mtgcollection.model.card.Card;
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
    public CardCollection addCardToCollection(int cardId, int collectionId) {
        final String sql = """
                insert into collection_card(collection_id,card_id,quantity)
                values(:collectionId, :cardId, 1);
                """;

        jdbcClient.sql(sql)
                .param("cardId", cardId)
                .param("collectionId", collectionId)
                .update();
        return new CardCollection(cardId,collectionId,1);
    }

    @Override
    public boolean updateCardInCollection(int cardId, int collectionId,int quantity) {
        final String sql = """
                update collection_card
                set quantity = :quantity
                where card_id = :cardId and collection_id = :collectionId;
                """;
        return jdbcClient.sql(sql)
                .param("quantity", quantity)
                .param("cardId", cardId)
                .param("collectionId",collectionId)
                .update() == 1;
    }

    @Override
    public boolean removeCardFromCollection(int cardId, int collectionId) {
        final String sql = """
                delete from collection_card where card_id = :cardId and collection_id = :collectionId;
                """;
        return jdbcClient.sql(sql)
                .param("cardId", cardId)
                .param("collectionId", collectionId)
                .update() == 1;
    }
}
