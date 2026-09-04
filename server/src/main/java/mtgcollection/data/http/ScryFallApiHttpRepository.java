package mtgcollection.data.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import mtgcollection.data.http.exceptions.DownstreamProvider;
import mtgcollection.data.http.response.model.CardResponse;
import mtgcollection.data.http.response.model.ErrorResponse;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Repository
public class ScryFallApiHttpRepository {
    public Optional<CardResponse> fetchCardFromScryfallByName(String cardName) throws InterruptedException {

        String parsedCardName = cardName.replace(" ", "%20");
        String url = String.format("https://api.scryfall.com/cards/named?fuzzy=%s",parsedCardName);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200){
                String json = response.body();
                ObjectMapper mapper = new ObjectMapper();
                CardResponse cardResponse = mapper.readValue(json,CardResponse.class);
                return Optional.of(cardResponse);
            }
            else if(response.statusCode() == 404){
                return Optional.empty();
            }
            else if(response.statusCode() >= 500){
                throw new DownstreamProvider("External API crashed with status of 500.");
            }
        } catch (IOException | InterruptedException e) {
            throw new InterruptedException();
        }
        return Optional.empty();
    }
}
