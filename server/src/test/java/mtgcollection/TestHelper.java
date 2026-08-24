package mtgcollection;


import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.Card;
import mtgcollection.model.User;

import java.util.List;
import java.util.UUID;

public class TestHelper {

    public static User userToFind(){
        return new User(1,"a@a.com","a");
    }

    public static User userToAdd(){
        return new User(0,"c@c.com","c");
    }

    public static LoggedInUser loggedInUser(){return new LoggedInUser(1,"a@a.com");}

    public static Card solRing(){return new Card(
            0,
            UUID.fromString("ab851e3a-7f61-464a-9520-21a4f02a3a10"),
            "Sol Ring",
            "{\"code\":\"C11\",\"name\":\"Commander 2011\"}",
            "[\"vintage\",\"commander\"]",
            "https://cards.scryfall.io/large/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
            null, // manaColor is null for colorless
            "{\"cmc\":1,\"mana_string\":\"{1}\"}",
            "Mark Tedin",
            3
    );}

    public static Card counterspell(){return new Card(
            0,
            UUID.fromString("a97f330d-6823-4337-a7d1-e9c522d08a5c"),
            "Counterspell",
            "{\"code\":\"MH2\",\"name\":\"Modern Horizons 2\"}",
            "[\"modern\",\"legacy\",\"vintage\",\"commander\",\"pauper\"]",
            "https://cards.scryfall.io/large/front/a/9/a97f330d-6823-4337-a7d1-e9c522d08a5c.jpg",
            "[\"U\"]",
            "{\"cmc\":2,\"mana_string\":\"{U}{U}\"}",
            "Mark Poole",
            1
    );}

    public static Card lightningBolt(){
        return new Card(
                0,
                UUID.fromString("f29ba16f-c8fb-42fe-aabf-87089cb214a7"), // cardUuid
                "Lightning Bolt", // name
                "{\"code\":\"2XM\",\"name\":\"Double Masters 2022\"}", // sets (JSON String)
                "[\"modern\",\"legacy\",\"vintage\",\"commander\",\"pauper\"]", // legalities (JSON String)
                "https://cards.scryfall.io/large/front/f/2/f29ba16f-c8fb-42fe-aabf-87089cb214a7.jpg", // imgPath
                "[\"R\"]", // manaColor (JSON String)
                "{\"cmc\":1,\"mana_string\":\"{R}\"}", // manaCost (JSON String)
                "Christopher Rush", // artistName
                1 // quantity
        );
    }

    public static List<Card> cardList(){
        return List.of(lightningBolt(), counterspell(), solRing());
    }

}
