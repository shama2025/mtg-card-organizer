package mtgcollection.data.jdbc;

import mtgcollection.data.interfaces.DeckRepository;
import mtgcollection.data.jdbc.mapper.DeckMapper;
import mtgcollection.model.Deck;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeckJdbcRepository implements DeckRepository {

    private final JdbcClient jdbcClient;

    public DeckJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Deck> fetchAllDecksInACollection(int collectionId) {
        final String sql = """
                select d.deck_id ,d.name ,d.date_created ,d.date_updated, sum(cd.quantity) as card_count from deck d
                inner join collection_deck cd2 on cd2.deck_id = d.deck_id and cd2.collection_id = :collectionId
                left join card_deck cd on cd.deck_id = d.deck_id
                group by d.deck_id, d.name, d.date_created, d.date_updated;
                """;
        return jdbcClient.sql(sql)
                .param("collectionId", collectionId)
                .query(new DeckMapper())
                .list();
    }

    @Override
    public Deck fetchDeckByDeckId(int deckId) {
        final String sql = """
                select d.deck_id ,d.name ,d.date_created ,d.date_updated, sum(cd.quantity) as card_count from deck d
                inner join collection_deck cd2 on cd2.deck_id = d.deck_id
                left join card_deck cd on cd.deck_id = d.deck_id
                where d.deck_id = :deckId
                group by d.deck_id, d.name, d.date_created, d.date_updated;
                """;
        return jdbcClient.sql(sql)
                .param("deckId", deckId)
                .query(new DeckMapper())
                .single();
    }
}
