create database if not exists mtg_collection_test;

use mtg_collection_test;

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
    name varchar(250) not null,
    img_path text not null,
    mana_color JSON null, # if null then color is colorless
    mana_cost JSON not null,
    sets text NOT null,
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
    constraint fk_deck_deck
    FOREIGN key(deck_id)
    REFERENCES deck(deck_id),
    constraint fk_deck_card
    FOREIGN key(card_id)
    references card(card_id)
);

create table if not EXISTS collection (
    collection_id int primary key auto_increment,
    card_id int not null,
    user_id int not null,
    constraint fk_user_collection
    FOREIGN key(user_id)
    REFERENCES `user`(user_id),
    constraint fk_card_collection
    FOREIGN key(card_id)
    references card(card_id)
);

DELIMITER //

DROP PROCEDURE IF EXISTS set_known_good_state //

CREATE PROCEDURE set_known_good_state()
BEGIN
    -- Delete bridge/child tables first to satisfy foreign key constraints
    delete from collection;
    delete from card_deck;
    delete from deck;
    delete from card;
    delete from `user`;
    
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
    insert into card (name, img_path, mana_color, mana_cost, sets, legalities, artist,quantity) values
        (
            'Lightning Bolt',
            'https://cards.scryfall.io/large/front/f/2/f29ba16f-c8fb-42fe-aabf-87089cb214a7.jpg',
            JSON_ARRAY('R'),
            JSON_OBJECT('cmc', 1, 'mana_string', '{R}'),
            'Double Masters 2022',
            JSON_ARRAY('modern', 'legacy', 'vintage', 'commander', 'pauper'),
            'Christopher Rush',
            1
        ),
        (
            'Counterspell',
            'https://cards.scryfall.io/large/front/a/9/a97f330d-6823-4337-a7d1-e9c522d08a5c.jpg',
            JSON_ARRAY('U'),
            JSON_OBJECT('cmc', 2, 'mana_string', '{U}{U}'),
           'Modern Horizons 2',
            JSON_ARRAY('modern', 'legacy', 'vintage', 'commander', 'pauper'),
            'Mark Poole',
            1
        ),
        (
            'Sol Ring',
            'https://cards.scryfall.io/large/front/a/b/ab851e3a-7f61-464a-9520-21a4f02a3a10.jpg',
            null, -- Colorless card
            JSON_OBJECT('cmc', 1, 'mana_string', '{1}'),
            'Commander 2021',
            JSON_ARRAY('vintage', 'commander'),
            'Mark Tedin',
            3
        ),
        (
            'Birds of Paradise',
            'https://cards.scryfall.io/large/front/f/e/fe0a79f6-e0b3-4705-894c-e83cb41c18ca.jpg',
            JSON_ARRAY('G'),
            JSON_OBJECT('cmc', 1, 'mana_string', '{G}'),
            'Dominaria Remastered',
            JSON_ARRAY('modern', 'legacy', 'vintage', 'commander'),
            'Edward P. Beard, Jr.',
            2
        );

    -- 3. Seed Decks
    insert into deck (name, card_count, date_created, date_updated) values
        ('Izzet Spellslinger', 60, '2026-08-01 10:00:00', '2026-08-20'),
        ('Mono Green Ramp', 100, '2026-08-15 09:15:00', '2026-08-24');

    -- 4. Seed Card-Deck Bridge (Links cards to decks)
    insert into card_deck (card_id, deck_id) values
        (1, 1), -- Lightning Bolt -> Izzet Spellslinger
        (2, 1), -- Counterspell -> Izzet Spellslinger
        (3, 1), -- Sol Ring -> Izzet Spellslinger
        (3, 2), -- Sol Ring -> Mono Green Ramp
        (4, 2); -- Birds of Paradise -> Mono Green Ramp

    -- 5. Seed Collections (Links cards to users)
    insert into collection (card_id, user_id) values
        (1, 1), -- User a owns Lightning Bolt
        (2, 1), -- User a owns Counterspell
        (3, 2), -- User b owns a Sol Ring
        (3, 1), -- User a owns Sol Ring
        (4, 2); -- User b owns Birds of Paradise

END //

DELIMITER ;