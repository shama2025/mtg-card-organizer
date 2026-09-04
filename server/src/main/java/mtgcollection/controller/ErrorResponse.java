package mtgcollection.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ResponseEntity<Object> build(Result<?> result) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (result.getResultType() == ResultType.NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(result.getErrorMessages(), status);
    }
}