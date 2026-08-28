package mtgcollection.data.jdbc;

import mtgcollection.data.interfaces.CardDeckRepository;
import mtgcollection.model.CardDeck;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
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

    @Override
    public CardDeck addCardDeck(int cardId, int deckId) {
        final String sql = """
                 insert into card_deck (card_id, deck_id, quantity) values
                 (:cardId, :deckId,1);
                """;

        jdbcClient.sql(sql)
                .param("cardId", cardId)
                .param("deckId", deckId)
                .update();
        return new CardDeck(cardId,deckId,1);
    }

    @Override
    public boolean updateCardInDeck(int cardId, int deckId, int quantity) {
        final String sql = """
                update card_deck cd
                set
                cd.quantity = :quantity
                where cd.card_id = :cardId and cd.deck_id = :deckId;
                """;
        return jdbcClient.sql(sql)
                .param("quantity", quantity)
                .param("cardId", cardId)
                .param("deckId",deckId)
                .update() == 1;
    }

    @Override
    public boolean removeCardFromDeck(int cardId, int deckId) {
        final String sql = """
                delete from card_deck cd where cd.card_id = :cardId and cd.deck_id = :deckId
                """;
        return jdbcClient.sql(sql)
                .param("cardId", cardId)
                .param("deckId",deckId)
                .update() == 1;
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
