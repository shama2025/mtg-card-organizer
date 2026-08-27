package mtgcollection;


import mtgcollection.data.http.response.model.CardResponse;
import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.*;
import mtgcollection.model.card.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static LoggedInUser loggedInUser(){return new LoggedInUser(1,"a@a.com", collection().getCollectionId());}

    public static Card solRing() {
        return new Card(
                0,
                UUID.fromString("ab851e3a-7f61-464a-9520-21a4f02a3a10"),
                "Sol Ring",
                new Set("C11", "Commander 2011"),
                List.of("Vintage","Commander"),
                List.of(
                        Map.of(
                        "small", "https://cards.scryfall.io/small/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                        "normal", "https://cards.scryfall.io/normal/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                        "large", "https://cards.scryfall.io/large/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                        "png", "https://cards.scryfall.io/png/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.png",
                        "art_crop", "https://cards.scryfall.io/art_crop/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg"
                )),
                null, // colorless
                new ManaCost(1, "{1}"),
                "Mark Tedin",
                3
        );
    }

    public static Card counterspell() {
        return new Card(
                0,
                UUID.fromString("a97f330d-6823-4337-a7d1-e9c522d08a5c"),
                "Counterspell",
                new Set("MH2", "Modern Horizons 2"),
                List.of("Modern", "Legacy", "Vintage", "Commander", "Pauper"),
                List.of(
                        Map.of(
                                "small", "https://cards.scryfall.io/small/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "normal", "https://cards.scryfall.io/normal/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "large", "https://cards.scryfall.io/large/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "png", "https://cards.scryfall.io/png/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.png",
                                "art_crop", "https://cards.scryfall.io/art_crop/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg"
                        )),
                new ManaColor(List.of("U")),
                new ManaCost(2, "{U}{U}"),
                "Mark Poole",
                1
        );
    }

    public static Card lightningBolt() {
        return new Card(
                1,
                UUID.fromString("f29ba16f-c8fb-42fe-aabf-87089cb214a7"),
                "Lightning Bolt",
                new Set("2XM", "Double Masters 2022"),
                List.of("Modern", "Legacy", "Vintage", "Commander", "Pauper"),
                List.of(
                        Map.of(
                                "small", "https://cards.scryfall.io/small/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "normal", "https://cards.scryfall.io/normal/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "large", "https://cards.scryfall.io/large/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "png", "https://cards.scryfall.io/png/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.png",
                                "art_crop", "https://cards.scryfall.io/art_crop/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg"
                        )),
                new ManaColor(List.of("R")),
                new ManaCost(1, "{R}"),
                "Christopher Rush",
                1
        );
    }

    public static Card birdsOfParadise() {
        return new Card(
                0,
                UUID.fromString("fe0a79f6-e0b3-4705-894c-e83cb41c18ca"),
                "Birds of Paradise",
                new Set("DMR", "Dominaria Remastered"),
                List.of("Modern", "Legacy", "Vintage", "Commander"),
                List.of(
                        Map.of(
                                "small", "https://cards.scryfall.io/small/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "normal", "https://cards.scryfall.io/normal/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "large", "https://cards.scryfall.io/large/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "png", "https://cards.scryfall.io/png/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.png",
                                "art_crop", "https://cards.scryfall.io/art_crop/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg"
                        )),
                new ManaColor(List.of("G")),
                new ManaCost(1, "{G}"),
                "Edward P. Beard, Jr.",
                2
        );
    }

    public static Card blackLotus() {
        return new Card(
                0,
                UUID.fromString("bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd"),
                "Black Lotus",
                new Set("vma", "Vintage Masters"),
                null, // Banned in all formats
                List.of(
                        Map.of(
                                "small", "https://cards.scryfall.io/small/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "normal", "https://cards.scryfall.io/normal/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "large", "https://cards.scryfall.io/large/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg",
                                "png", "https://cards.scryfall.io/png/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.png",
                                "art_crop", "https://cards.scryfall.io/art_crop/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg"
                        )),
                new ManaColor(null),
                new ManaCost(0, "{0}"),
                "Christopher Rush",
                1
        );
    }

    public static List<Card> cardList() {
        return List.of(lightningBolt(), counterspell(), solRing());
    }

    public static CardResponse blackLotusCardResponse() {
        // Image URIs matching the Scryfall paths used in TestHelper.blackLotus()
        HashMap<String, String> imageUris = new HashMap<>();
        imageUris.put("small", "https://cards.scryfall.io/small/front/b/d/bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd.jpg");
        imageUris.put("normal", "https://cards.scryfall.io/small/front/b/d/bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd.jpg");
        imageUris.put("large", "https://cards.scryfall.io/small/front/b/d/bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd.jpg");
        imageUris.put("png", "https://cards.scryfall.io/small/front/b/d/bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd.jpg");
        imageUris.put("art_crop", "https://cards.scryfall.io/small/front/b/d/bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd.jpg");

        // Legalities set to not_legal / banned across formats
        HashMap<String, String> legalities = new HashMap<>();
        legalities.put("standard", "not_legal");
        legalities.put("modern", "not_legal");
        legalities.put("legacy", "banned");
        legalities.put("vintage", "restricted");
        legalities.put("commander", "banned");

        return new CardResponse(
                "card",                                                                // object
                UUID.fromString("bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd"),               // cardId
                UUID.fromString("6a0429f2-2b3f-4e00-8800-000000000000"),               // oracleId
                new int[]{382866},                                                     // multiverseIds
                "",                                                                    // resourceId
                0,                                                                     // mtgoId
                0,                                                                     // arenaId
                0,                                                                     // tcgPlayerId
                0,                                                                     // cardMarkId
                "Black Lotus",                                                         // name
                "en",                                                                  // language
                "2014-06-16",                                                          // releasedAt
                "https://api.scryfall.com/cards/bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd",   // uri
                "https://scryfall.com/card/vma/4/black-lotus",                        // scryfallUri
                "normal",                                                              // layout
                true,                                                                  // highResImage
                "highres_scan",                                                        // imageStatus
                "",                                                                    // imageUpdatedAt
                imageUris,                                                             // imageUris
                "{0}",                                                                 // manaCost
                0.0,                                                                   // cmc
                "Artifact",                                                            // typeLine
                "{T}, Sacrifice Black Lotus: Add three mana of any one color.",        // oracleText
                null,                                                                  // power
                null,                                                                  // toughness
                List.of(),                                                        // colors (colorless)
                new String[]{},                                                        // colorIdentity
                new String[]{},                                                        // keywords
                new String[]{"W", "U", "B", "R", "G"},                                 // producedMana
                legalities,                                                            // legalities
                new String[]{"mtgo"},                                                  // games
                true,                                                                  // reserved
                false,                                                                 // gameChanger
                true,                                                                  // foil
                false,                                                                 // nonfoil
                new String[]{"foil"},                                                  // finishes
                false,                                                                 // oversized
                false,                                                                 // promo
                true,                                                                  // reprint
                false,                                                                 // variation
                UUID.randomUUID(),                                                     // setId
                "vma",                                                                 // set
                "Vintage Masters",                                                     // setName
                "masters",                                                             // setType
                "",                                                                    // setUri
                "",                                                                    // setSearchUri
                "",                                                                    // scryFallSetUri
                "",                                                                    // rulingsUri
                "",                                                                    // printsSearchUri
                null,                                                                  // watermark
                new String[]{},                                                        // frameEffects
                null,                                                                  // securityStamp
                "4",                                                                   // collectorNumber
                true,                                                                  // digital
                "rare",                                                                // rarity
                "",                                                                    // flavorText
                null,                                                                  // cardBackId
                "Christopher Rush",                                                    // artist
                new UUID[]{},                                                          // artistId
                null,                                                                  // illustrationId
                "black",                                                               // borderColor
                "2015",                                                                // frame
                false,                                                                 // fullArt
                false,                                                                 // textless
                true,                                                                  // booster
                false,                                                                 // storySpotlight
                new String[]{},                                                        // promoTypes
                0,                                                                     // edhrecRank
                new HashMap<>(),                                                       // prices
                new HashMap<>(),                                                       // relatedUris
                new HashMap<>(),                                                       // purchaseUris
                0,                                                                     // pennyRank
                List.of(),                                                             // cardFaces
                List.of(),                                                             // allParts
                new HashMap<>(),                                                       // preview
                0
        );
    }

    public static Deck user2Deck(){
        return new Deck(2,"Mono Green Ramp",5,
                LocalDate.of(2026,8,15),LocalDate.of(2026,8,24),null);
    }

    public static Deck user2DeckWithCard(){
        return new Deck(2,"Mono Green Ramp",5,
                LocalDate.of(2026,8,15),LocalDate.of(2026,8,24),List.of(lightningBolt()));
    }

    public static Deck deckToCreate(){
        return new Deck(0, "Temur Landfall",0,LocalDate.now(),null,null);
    }

    public static Deck deckToEdit(){return new Deck(1,"Izzet Spellslinger",
            60, LocalDate.of(2026,8,1), LocalDate.of(2026,8,20),TestHelper.cardList());}
}
