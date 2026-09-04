package mtgcollection.controller;

import mtgcollection.data.http.exceptions.DownstreamProvider;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalErrorHandler {

    // TODO: Add additional exception handlers here

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException ex) {
        return new ResponseEntity<>(new ErrorResponse("The local Ollama server is not reachable. Please check if it is running."),HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Catches Spring AI transient errors (timeouts, temporary overloads)
    @ExceptionHandler(TransientAiException.class)
    public ResponseEntity<ErrorResponse> handleTransientAiException(TransientAiException ex) {
        return new ResponseEntity<>(new ErrorResponse("Ollama is busy or taking too long to respond. Try again shortly."),HttpStatus.GATEWAY_TIMEOUT);
    }

    // Catches Spring AI non-transient errors (model not found, bad requests)
    @ExceptionHandler(NonTransientAiException.class)
    public ResponseEntity<ErrorResponse> handleNonTransientAiException(NonTransientAiException ex) {
        return new ResponseEntity<>(new ErrorResponse("Ollama Model Error"),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DownstreamProvider.class)
    public ResponseEntity<ErrorResponse> handleException(DownstreamProvider ex){
        return new ResponseEntity<>(new ErrorResponse("Sorry, something went wrong with the Scryfall API."),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<ErrorResponse> handleException(InterruptedException ex){
        return new ResponseEntity<>(new ErrorResponse("Error parsing scryfall response."),HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException ex){
    return new ResponseEntity<>(new ErrorResponse("Invalid JSON."),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpMediaTypeNotSupportedException ex){
        return new ResponseEntity<>(new ErrorResponse("HTTP media not supported"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // TODO log this exception
        ex.printStackTrace();
        return new ResponseEntity<>(
                new ErrorResponse("Sorry, something unexpected went wrong."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}