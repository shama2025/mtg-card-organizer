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
        String token = responseHeader.get("authorization");
        // Confirm the token is valid
        if(!JwtHandler.validateToken(token)) {
            return new ResponseEntity<>(List.of("Tampered tokens."), HttpStatus.UNAUTHORIZED);
        }
        // token is valid
        return null;
    }

}
