package mtgcollection.dto;

public record LoggedInUser(int id, String email,int collectionId, String token) {
}
