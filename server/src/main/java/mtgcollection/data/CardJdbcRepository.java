package mtgcollection.data;

import mtgcollection.data.mapper.CardMapper;
import mtgcollection.model.Card;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
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
                card.mana_color, card.mana_cost, card.sets, card.legalities, card.artist, quantity from card
                inner join collection on collection.card_id = card.card_id
                inner join `user` on `user`.user_id = collection.user_id
                where `user`.user_id = :userId
                """;
        return jdbcClient.sql(sql)
                .param("userId", userId)
                .query(new CardMapper())
                .list();
    }

    public Card addCard(Card card){
        final String sql = """
                insert into card (card_uuid, name, img_path, mana_color, mana_cost, sets, legalities, artist, quantity)
                values (:cardUuid, :name, :imgPath, JSON_ARRAY(:manaColor), JSON_OBJECT('cmc',:cmc, 'mana_string', :manaString),
                JSON_OBJECT('code', :code, 'name', :name), JSON_ARRAY(:legalities), :artist, :quantity);
                """;
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

       String[] colors;
        try{
            colors = card.getManaColor().colors();
        }catch (NullPointerException ex){
            colors = null;
        }

        jdbcClient.sql(sql)
                .param("cardUuid", card.getCardId().toString())
                .param("name", card.getName())
                .param("imgPath", card.getImgPath())
                .param("manaColor", colors)
                .param("cmc", card.getManaCost().convertedManaCost())
                .param("manaString",card.getManaCost().manaString())
                .param("code",card.getSet().code())
                .param("name",card.getSet().name())
                .param("legalities", card.getLegalities().toString())
                .param("artist", card.getArtistName())
                .param("quantity", card.getQuantity())
                .update(keyHolder,"card_id");
        card.setId(keyHolder.getKey().intValue());
        return card;
    }
}
