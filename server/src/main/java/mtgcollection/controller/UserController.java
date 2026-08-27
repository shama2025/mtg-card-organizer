package mtgcollection.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mtgcollection.domain.UserService;
import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.Result;
import mtgcollection.model.User;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> findUserByEmail(@RequestHeader Map<String,String> headers) throws JsonProcessingException {

        // TODO: Additional header checks
        // Check if id has been manipulated
        if(headers.get("authorization") == null){
            // Missing auth header
            return new ResponseEntity<>(List.of("Missing authorization header"),HttpStatus.BAD_REQUEST);
        }

        // Extract json from string
        String userAuthJson = headers.get("authorization");


        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(userAuthJson,User.class);
        Result<LoggedInUser> result = service.findUserByEmail(user);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getpayload(),HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody User user){
        Result<LoggedInUser> result = service.add(user);

        if(result.isSuccess()){
            return new ResponseEntity<>(result.getpayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

}
