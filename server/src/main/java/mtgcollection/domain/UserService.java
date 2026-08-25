package mtgcollection.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import mtgcollection.data.interfaces.UserRepository;
import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import mtgcollection.model.User;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public Result<LoggedInUser> findUserByEmail(User user){
        Result<LoggedInUser> result = new Result<>();

        validate(result,user);

        // Fetch user
        User fetchedUser = repository.findUserByEmail(user.getEmail());

        if(fetchedUser == null){
            result.addErrorMessage("User does not exist.", ResultType.NOT_FOUND);
            return result;
        }

        // Compare passwords
        if(!fetchedUser.checkPassword(user.getPassword())){
            result.addErrorMessage("Incorrect password.", ResultType.INVALID);
            return result;
        }

        result.setpayload(new LoggedInUser(fetchedUser.getUserId(),fetchedUser.getEmail()));
        return result;
    }

    public Result<User> add(User user){
        Result<User> result = new Result<>();

        validate(result,user);

        // Confirm email is unique
        try{
            // Hash User password first
            user.hashPassword();
            User createdUser = repository.add(user);
            result.setpayload(createdUser);
            return result;
        }catch(DataIntegrityViolationException ex){
            result.addErrorMessage("Email already exists.", ResultType.INVALID);
        }

        return result;
    }

    private void validate(Result<?> result,User user){
        // Validate using validators first
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<User> violation : violations) {
                result.addErrorMessage(violation.getMessage(), ResultType.INVALID);
            }
        }
    }
}
