package mtgcollection.model.card;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Card {
    private int cardId;
    private UUID cardUuid;
    private String name;
    private List<String> legalities;
    private Set sets;
    private Map<String,String> imagePaths;
    private ManaColor manaColor;
    private ManaCost manaCost;
    private String artistName;
    private int quantity;

    public Card(){}

    public Card(int cardId, UUID cardUuid, String name, Set sets, List<String> legalities, Map<String,String> imagePaths, ManaColor manaColor, ManaCost manaCost, String artistName, int quantity){
        this.cardId = cardId;
        this.cardUuid = cardUuid;
        this.name = name;
        this.sets = sets;
        this.legalities = legalities;
        this.imagePaths = imagePaths;
        this.manaColor = manaColor;
        this.manaCost = manaCost;
        this.artistName = artistName;
        this.quantity = quantity;
    }

    public int getId() {
        return cardId;
    }

    public void setId(int id) {
            this.cardId = id;
    }

    public UUID getCardId() {
        return cardUuid;
    }

    public void setCardId(UUID cardId) {
        this.cardUuid = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLegalities() {
        return legalities;
    }

    public void setLegalities(List<String> legalities) {
        this.legalities = legalities;
    }

    public Map<String,String> getImgPath() {
        return imagePaths;
    }

    public void setImgPath(Map<String,String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public Set getSet() {
        return sets;
    }

    public void setSet(Set sets) {
        this.sets = sets;
    }

    public ManaColor getManaColor() {
        return manaColor;
    }

    public void setManaColor(ManaColor manaColor) {
        this.manaColor = manaColor;
    }

    public ManaCost getManaCost() {
        return manaCost;
    }

    public void setManaCost(ManaCost manaCost) {
        this.manaCost = manaCost;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cardId == card.cardId && quantity == card.quantity && Objects.equals(cardUuid, card.cardUuid) && Objects.equals(name, card.name) && Objects.equals(legalities, card.legalities) && Objects.equals(sets, card.sets) && Objects.equals(imagePaths, card.imagePaths) && Objects.equals(manaColor, card.manaColor) && Objects.equals(manaCost, card.manaCost) && Objects.equals(artistName, card.artistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, cardUuid, name, legalities, sets, imagePaths, manaColor, manaCost, artistName, quantity);
    }

    public void parseSet(String code, String name){
        this.sets = new Set(code,name);
    }

    public void parseLegalities(HashMap<String,String> formats){
         this.legalities = formats.entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase("legal"))
                .map(Map.Entry::getKey)
                 .collect(Collectors.toList());
    }

    public void parseCardImages(HashMap<String, String> imageUris){
//        HashMap<String,String> paths = new ArrayList<>();
//        for(Map.Entry<String, String> imageUri : imageUris.entrySet()){
//            String type = imageUri.getKey();
//            String uri = imageUri.getValue();
//            paths.add(new ImagePaths(type,uri));
//        }
        this.imagePaths = imageUris;
    }

    public void parseCardManaCost(String manaCost){
        if (manaCost == null || manaCost.isBlank()) {
            this.manaCost = new ManaCost(0,"");
        }
        int cmc = 0;
        Matcher matcher = Pattern.compile("\\{([^}]+)\\}").matcher(manaCost);
        while (matcher.find()) {
            String symbol = matcher.group(1).toUpperCase();

            if (symbol.matches("\\d+")) {
                cmc += Integer.parseInt(symbol);
            } else if (symbol.equals("X") || symbol.equals("Y") || symbol.equals("Z")) {
                cmc += 0;
            } else if (symbol.startsWith("2/")) {
                cmc += 2;
            } else {
                cmc += 1;
            }
        }
        this.manaCost = new ManaCost(cmc,manaCost);
    }
}
