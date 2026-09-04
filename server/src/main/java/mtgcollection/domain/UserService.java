package mtgcollection.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import mtgcollection.controller.JwtHandler;
import mtgcollection.data.interfaces.CollectionRepository;
import mtgcollection.model.Collection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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

    private final CollectionRepository collectionRepository;

    private final JwtHandler jwtHandler;

    public UserService(UserRepository repository, CollectionRepository collectionRepository ,JwtHandler jwtHandler){
        this.repository = repository;
        this.collectionRepository = collectionRepository;
        this.jwtHandler = jwtHandler;
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
        Collection collection;
        try{
            collection = collectionRepository.fetchUserCollection(fetchedUser.getUserId());
            if(collection == null){
                result.addErrorMessage("No collection associated with user.",ResultType.NOT_FOUND);
                return result;
            }
        }catch (EmptyResultDataAccessException ex){
            result.addErrorMessage("No collection associated with user.",ResultType.NOT_FOUND);
            return  result;
        }
        String token = jwtHandler.generateToken(fetchedUser.getEmail());
        result.setpayload(new LoggedInUser(fetchedUser.getUserId(),fetchedUser.getEmail(),collection.getCollectionId(),token));
        return result;
    }

    public Result<LoggedInUser> add(User user){
        Result<LoggedInUser> result = new Result<>();

        validate(result,user);
        if(!result.isSuccess()){
            return result;
        }
        // Confirm email is unique
        try{
            // Hash User password first
            user.hashPassword();
            User createdUser = repository.add(user);
            Collection collection = collectionRepository.createCollection(user.getUserId());
            String token = jwtHandler.generateToken(createdUser.getEmail());
            result.setpayload(new LoggedInUser(createdUser.getUserId(), createdUser.getEmail(), collection.getCollectionId(),token));
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
