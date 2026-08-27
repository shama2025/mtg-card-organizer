package mtgcollection.data.jdbc;

import mtgcollection.data.interfaces.CardDeckRepository;
import mtgcollection.model.CardDeck;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CardDeckJdbcRepository implements CardDeckRepository {

    private final JdbcClient jdbcClient;

    public CardDeckJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<CardDeck> fetchAllCardDecksFromDeckId(int deckId) {
        final String sql = """
                select cd.card_id, cd.deck_id, cd.quantity from card_deck cd
                where cd.deck_id = :deckId;
                """;
        return jdbcClient.sql(sql)
                .param("deckId",deckId)
                .query(CardDeck.class)
                .list();
    }

    @Transactional
    @Override
    public boolean removeDeck(int deckId) {
        final String sql = """
                delete from card_deck where deck_id = :deckId;
                """;
        return jdbcClient.sql(sql)
                .param("deckId",deckId)
                .update() > 0;
    }
}
