package mtgcollection;


import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.*;

import java.text.Format;
import java.util.List;
import java.util.UUID;

public class TestHelper {

    public static User userToFind(){
        return new User(1,"a@a.com","a");
    }

    public static User userToAdd(){
        return new User(0,"c@c.com","c");
    }

    public static Collection collection(){
        return new Collection(1,userToFind().getUserId());
    }

    public static LoggedInUser loggedInUser(){return new LoggedInUser(1,"a@a.com");}

    public static Card solRing(){return new Card(
            0,
            UUID.fromString("ab851e3a-7f61-464a-9520-21a4f02a3a10"),
            "Sol Ring",
            new Set("C11","Commander 2011"),
            List.of(Formats.VINTAGE,Formats.COMMANDER),
            "https://cards.scryfall.io/large/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
            null, // manaColor is null for colorless
            new ManaCost(1,"{1}"),
            "Mark Tedin",
            3
    );}

    public static Card counterspell(){
        return new Card(
            0,
            UUID.fromString("a97f330d-6823-4337-a7d1-e9c522d08a5c"),
            "Counterspell",
            new Set("MH2","Modern Horizons 2"),
            List.of(Formats.MODERN,Formats.LEGACY,
                        Formats.VINTAGE,Formats.COMMANDER,Formats.PAUPER),
            "https://cards.scryfall.io/large/front/a/9/a97f330d-6823-4337-a7d1-e9c522d08a5c.jpg",
            new ManaColor(List.of("U").toArray(new String[0])),
            new ManaCost(2,"{U}{U}"),
            "Mark Poole",
            1
    );}

    public static Card lightningBolt(){
        return new Card (
                0,
                UUID.fromString("f29ba16f-c8fb-42fe-aabf-87089cb214a7"), // cardUuid
                "Lightning Bolt", // name
                new Set("2XM", "Double Masters 2022"), // sets (JSON String)
                List.of(Formats.MODERN,Formats.LEGACY,
                        Formats.VINTAGE,Formats.COMMANDER,Formats.PAUPER), // legalities (JSON String)
                "https://cards.scryfall.io/large/front/f/2/f29ba16f-c8fb-42fe-aabf-87089cb214a7.jpg", // imgPath
                new ManaColor(List.of("R").toArray(new String[0])), // manaColor (JSON String)
                new ManaCost(1,"{R}"), // manaCost (JSON String)
                "Christopher Rush", // artistName
                1 // quantity
        );
    }

    public static List<Card> cardList() {
        return List.of(lightningBolt(), counterspell(), solRing());
    }
}
