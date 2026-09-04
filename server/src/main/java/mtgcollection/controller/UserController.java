package mtgcollection.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mtgcollection.domain.UserService;
import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.Result;
import mtgcollection.model.User;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> findUserByEmail(@RequestBody User user) {
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
