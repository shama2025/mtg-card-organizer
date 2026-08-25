package mtgcollection.data.http.exceptions;

public class DownstreamProvider extends RuntimeException {
    public DownstreamProvider(String message) {
        super(message);
    }
}
