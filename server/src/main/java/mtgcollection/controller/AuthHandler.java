package mtgcollection.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import mtgcollection.dto.LoggedInUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class AuthHandler {

    public static ResponseEntity<?> auth(Map<String,String> responseHeader) throws JsonProcessingException {
        // Check if the auth headers are present
        if(responseHeader.get("authorization") == null){
            return new ResponseEntity<>(List.of("Missing required headers."), HttpStatus.BAD_REQUEST);
        }
        String auth = responseHeader.get("authorization");
        LoggedInUser loggedInUser = new ObjectMapper().readValue(auth, LoggedInUser.class);
        // Confirm the token is valid
        if(!JwtHandler.validateToken(loggedInUser.token())){
            return new ResponseEntity<>(List.of("Invalid tokens."), HttpStatus.UNAUTHORIZED);
        }
        // Confirm the token matches the user email
        String email = JwtHandler.getUsernameFromToken(loggedInUser.token());
        if(!email.equals(loggedInUser.email())){
            return new ResponseEntity<>(List.of("Tampered tokens."),HttpStatus.UNAUTHORIZED);
        }
        // token is valid
        return null;
    }

}
