package mtgcollection.data.http.response.model;

public record ErrorResponse(
        String object,
        String code,
        String type,
        int status,
        String details
) {}