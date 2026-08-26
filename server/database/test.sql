create database if not exists mtg_collection_test;

use mtg_collection_test;

DROP TABLE IF EXISTS collection_card;
DROP TABLE IF EXISTS collection;
DROP TABLE IF EXISTS card_deck;
DROP TABLE IF EXISTS deck;
DROP TABLE IF EXISTS card;
DROP TABLE IF EXISTS `user`;

create table if not EXISTS `user` (
    user_id int primary key auto_increment,
    email varchar(255) not null UNIQUE,
    password varchar(60) not null
);

CREATE table if not exists card(
    card_id int PRIMARY key auto_increment,
    card_uuid varchar(36) not null,
    name varchar(250) not null,
    img_path JSON not null,
    mana_color JSON null, # if null then color is colorless
    mana_cost JSON not null,
    sets JSON NOT null,
    legalities JSON not null, # Only holds legal formats
    artist varchar(250) not null,
    quantity int not null
);

create table if not EXISTS deck(
    deck_id int primary key auto_increment,
    name varchar(100) not null,
    card_count int not null,
    date_created date not null,
    date_updated date not null
);

create table if not exists card_deck(
    card_id int not null,
    deck_id int not null,
    quantity int not null,
    constraint chk_card_deck_quantity check(quantity >= 0),
    constraint fk_deck_deck_id
    FOREIGN key(deck_id)
    references deck(deck_id),
    constraint fk_deck_card_id
    FOREIGN key(card_id)
    references card(card_id)
);

create table if not EXISTS collection (
    collection_id int primary key auto_increment,
    user_id int not null unique,
    constraint fk_user_collection
    FOREIGN key(user_id)
    REFERENCES `user`(user_id)
);
	
create table if not exists collection_card(
    collection_id int not null,
    card_id int not NULL,
    quantity int not null,
    constraint chk_card_quantity check(quantity >= 0),
    constraint fk_collection_collection_id
    foreign key(collection_id)
    references collection(collection_id),
    constraint fk_collection_user_id
    FOREIGN key(card_id)
    REFERENCES card(card_id)
);

DELIMITER //

DROP PROCEDURE IF EXISTS set_known_good_state //

CREATE PROCEDURE set_known_good_state()
BEGIN
    delete from collection_card;
    delete from collection;
    delete from card_deck;
    delete from deck;
    delete from card;
    delete from `user`;
    
    alter table collection_card auto_increment = 1;
    alter table collection auto_increment = 1;
    alter table card_deck auto_increment = 1;
    alter table deck auto_increment = 1;
    alter table card auto_increment = 1;
    alter table `user` auto_increment = 1;

    -- 1. Seed Users
    insert into `user` (email, password) values
        ('a@a.com', 'a'),
        ('b@b.com', 'b');

    -- 2. Seed Cards
    insert into card (card_uuid, name, img_path, mana_color, mana_cost, sets, legalities, artist, quantity) values
        (
            'f29ba16f-c8fb-42fe-aabf-87089cb214a7',
            'LIGHTNING BOLT',
            JSON_OBJECT(
                'small', 'https://cards.scryfall.io/small/front/f/2/f29ba16f-c8fb-42fe-aabf-87089cb214a7.jpg',
                'normal', 'https://cards.scryfall.io/normal/front/f/2/f29ba16f-c8fb-42fe-aabf-87089cb214a7.jpg',
                'large', 'https://cards.scryfall.io/large/front/f/2/f29ba16f-c8fb-42fe-aabf-87089cb214a7.jpg',
                'png', 'https://cards.scryfall.io/png/front/f/2/f29ba16f-c8fb-42fe-aabf-87089cb214a7.png',
                'art_crop', 'https://cards.scryfall.io/art_crop/front/f/2/f29ba16f-c8fb-42fe-aabf-87089cb214a7.jpg'
            ),
            JSON_ARRAY('R'),
            JSON_OBJECT('cmc', 1, 'mana_string', '{R}'),
            JSON_OBJECT('code','2XM','name','Double Masters 2022'),
            JSON_ARRAY('modern', 'legacy', 'vintage', 'commander', 'pauper'),
            'Christopher Rush',
            1
        ),
        (
            'a97f330d-6823-4337-a7d1-e9c522d08a5c',
            'COUNTERSPELL',
            JSON_OBJECT(
                'small', 'https://cards.scryfall.io/small/front/a/9/a97f330d-6823-4337-a7d1-e9c522d08a5c.jpg',
                'normal', 'https://cards.scryfall.io/normal/front/a/9/a97f330d-6823-4337-a7d1-e9c522d08a5c.jpg',
                'large', 'https://cards.scryfall.io/large/front/a/9/a97f330d-6823-4337-a7d1-e9c522d08a5c.jpg',
                'png', 'https://cards.scryfall.io/png/front/a/9/a97f330d-6823-4337-a7d1-e9c522d08a5c.png',
                'art_crop', 'https://cards.scryfall.io/art_crop/front/a/9/a97f330d-6823-4337-a7d1-e9c522d08a5c.jpg'
            ),
            JSON_ARRAY('U'),
            JSON_OBJECT('cmc', 2, 'mana_string', '{U}{U}'),
            JSON_OBJECT('code','MH2','name','Modern Horizons 2'),
            JSON_ARRAY('modern', 'legacy', 'vintage', 'commander', 'pauper'),
            'Mark Poole',
            1
        ),
        (
            'ab851e3a-7f61-464a-9520-21a4f02a3a10',
            'SOL RING',
            JSON_OBJECT(
                'small', 'https://cards.scryfall.io/small/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg',
                'normal', 'https://cards.scryfall.io/normal/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg',
                'large', 'https://cards.scryfall.io/large/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg',
                'png', 'https://cards.scryfall.io/png/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.png',
                'art_crop', 'https://cards.scryfall.io/art_crop/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg'
            ),
            null, -- Colorless card
            JSON_OBJECT('cmc', 1, 'mana_string', '{1}'),
            JSON_OBJECT('code','C11','name','Commander 2011'),
            JSON_ARRAY('vintage', 'commander'),
            'Mark Tedin',
            3
        ),
        (
            'fe0a79f6-e0b3-4705-894c-e83cb41c18ca',
            'BIRDS OF PARADISE',
            JSON_OBJECT(
                'small', 'https://cards.scryfall.io/small/front/f/e/fe0a79f6-e0b3-4705-894c-e83cb41c18ca.jpg',
                'normal', 'https://cards.scryfall.io/normal/front/f/e/fe0a79f6-e0b3-4705-894c-e83cb41c18ca.jpg',
                'large', 'https://cards.scryfall.io/large/front/f/e/fe0a79f6-e0b3-4705-894c-e83cb41c18ca.jpg',
                'png', 'https://cards.scryfall.io/png/front/f/e/fe0a79f6-e0b3-4705-894c-e83cb41c18ca.png',
                'art_crop', 'https://cards.scryfall.io/art_crop/front/f/e/fe0a79f6-e0b3-4705-894c-e83cb41c18ca.jpg'
            ),
            JSON_ARRAY('G'),
            JSON_OBJECT('cmc', 1, 'mana_string', '{G}'),
            JSON_OBJECT('code','DMR','name','Dominaria Remastered'),
            JSON_ARRAY('modern', 'legacy', 'vintage', 'commander'),
            'Edward P. Beard, Jr.',
            2
        );

    -- 3. Seed Decks
    insert into deck (name, card_count, date_created, date_updated) values
        ('Izzet Spellslinger', 60, '2026-08-01', '2026-08-20'),
        ('Mono Green Ramp', 100, '2026-08-15', '2026-08-24');

    -- 4. Seed Card-Deck Bridge (Links cards to decks with quantities)
    insert into card_deck (card_id, deck_id, quantity) values
        (1, 1, 4), -- 4x Lightning Bolt -> Izzet Spellslinger
        (2, 1, 4), -- 4x Counterspell -> Izzet Spellslinger
        (3, 1, 1), -- 1x Sol Ring -> Izzet Spellslinger
        (3, 2, 1), -- 1x Sol Ring -> Mono Green Ramp
        (4, 2, 4); -- 4x Birds of Paradise -> Mono Green Ramp

    -- 5. Seed Collections
    insert into collection (user_id) values
        (1), -- collection_id 1 for User 1
        (2); -- collection_id 2 for User 2

    -- 6. Seed Collection Cards (Join table)
    insert into collection_card (collection_id, card_id, quantity) values
        (1, 1, 1), -- User 1 owns Lightning Bolt
        (1, 2, 1), -- User 1 owns Counterspell
        (1, 3, 1), -- User 1 owns Sol Ring
        (2, 3, 3), -- User 2 owns 3 Sol Rings
        (2, 4, 2); -- User 2 owns 2 Birds of Paradise

END //

DELIMITER ;