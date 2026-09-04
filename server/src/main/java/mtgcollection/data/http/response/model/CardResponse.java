package mtgcollection.data.http.response.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public record CardResponse(
String object,

@JsonProperty("id")
UUID cardId,

@JsonProperty("oracle_id")
UUID oracleId,

@JsonProperty("multiverse_ids")
int [] multiverseIds,

@JsonProperty("resource_id")
String resourceId,

@JsonProperty("mtgo_id")
int mtgoId,

@JsonProperty("arena_id")
int arenaId,

@JsonProperty("tcgplayer_id")
int tcgPlayerId,

@JsonProperty("cardmarket_id")
int cardMarkId,

String name,

@JsonProperty("lang")
String language,

@JsonProperty("released_at")
String releasedAt,

String uri,

@JsonProperty("scryfall_uri")
String scryfallUri,

String layout,

@JsonProperty("highres_image")
boolean highResImage,

@JsonProperty("image_status")
String imageStatus,

@JsonProperty("image_updated_at")
String imageUpdatedAt,

@JsonProperty("image_uris")
HashMap<String,String> imageUris,

@JsonProperty("mana_cost")
String manaCost,

Double cmc,

@JsonProperty("type_line")
String typeLine,

@JsonProperty("oracle_text")
String oracleText,

String power,

String toughness,

List<String> colors,// if array is empty then card is colorless

@JsonProperty("color_identity")
String [] colorIdentity,

String [] keywords,

@JsonProperty("produced_mana")
String [] producedMana,

HashMap<String,String> legalities,
String [] games,
boolean reserved,

@JsonProperty("game_changer")
boolean gameChanger,
boolean foil,

@JsonProperty("nonfoil")
boolean nonfoil,

String [] finishes,

boolean oversized,

boolean promo,

boolean reprint,

boolean variation,

@JsonProperty("set_id")
UUID setId,
String set,

@JsonProperty("set_name")
String setName,

@JsonProperty("set_type")
String setType,

@JsonProperty("set_uri")
String setUri,

@JsonProperty("set_search_uri")
String setSearchUri,

@JsonProperty("scryfall_set_uri")
String scryFallSetUri,

@JsonProperty("rulings_uri")
String rulingsUri,

@JsonProperty("prints_search_uri")
String printsSearchUri,

String watermark,

@JsonProperty("frame_effects")
String [] frameEffects,

@JsonProperty("security_stamp")
String securityStamp,

@JsonProperty("collector_number")
String collectorNumber,

boolean digital,

String rarity,

@JsonProperty("flavor_text")
String flavorText,

@JsonProperty("card_back_id")
UUID cardBackId,

String artist,

@JsonProperty("artist_ids")
UUID [] artistId,

@JsonProperty("illustration_id")
UUID illustrationId,

@JsonProperty("border_color")
String borderColor,

String frame,

@JsonProperty("full_art")
boolean fullArt,

boolean textless,

boolean booster,

@JsonProperty("story_spotlight")
boolean storySpotlight,

@JsonProperty("promo_types")
String [] promoTypes,

@JsonProperty("edhrec_rank")
int edhrecRank,

HashMap<String,String> prices,

@JsonProperty("related_uris")
HashMap<String,String> relatedUris,

@JsonProperty("purchase_uris")
HashMap<String,String> purchaseUris,

@JsonProperty("penny_rank")
int pennyRank,

@JsonProperty("card_faces")
List<HashMap<String,Object>> cardFaces,

@JsonProperty("all_parts")
List<HashMap<String,Object>> allParts,

HashMap<String,String> preview,

@JsonProperty("mtgo_foil_id")
int mtgoFoilId
) { }