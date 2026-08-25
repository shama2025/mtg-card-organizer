package mtgcollection.data.jdbc;

import mtgcollection.data.interfaces.CardRepository;
import mtgcollection.data.jdbc.mapper.CardMapper;
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

    public List<Card> fetchAllCards(int collectionId){
        final String sql = """
                select c.card_id ,c.card_uuid, c.name, c.img_path,
                c.mana_color, c.mana_cost, c.sets,\s
                c.legalities, c.artist, cc.quantity from card c
                inner join collection_card cc on cc.card_id = c.card_id\s
                inner join collection c2 on c2.collection_id = cc.collection_id\s
                inner join `user` u on u.user_id = c2.user_id
                where c2.collection_id = :collectionId;
                """;
        return jdbcClient.sql(sql)
                .param("collectionId", collectionId)
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
