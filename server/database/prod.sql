use mtg_collection;

DROP TABLE IF EXISTS deck_card;
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
	constraint chk_card_deck_quantity check(quantity >=0),
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
	constraint chk_card_quantity check(quantity>=0),
	constraint fk_collection_collection_id
	foreign key(collection_id)
	references collection(collection_id),
	constraint fk_collection_user_id
	FOREIGN key(card_id)
	REFERENCES card(card_id)
	);
