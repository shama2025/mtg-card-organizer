package mtgcollection.domain;

import mtgcollection.controller.JwtHandler;
import mtgcollection.data.interfaces.CollectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import mtgcollection.TestHelper;
import mtgcollection.data.interfaces.UserRepository;
import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import mtgcollection.model.User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {

    @MockitoBean
    UserRepository repository;

    @MockitoBean
    CollectionRepository collectionRepository;

    @Autowired
    UserService service;

    @Autowired
    JdbcClient jdbcClient;

    @Autowired
    JwtHandler jwtHandler;

    @BeforeEach
    void runSetKnownGoodState(){
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Nested
    class FindUserByEmailTests{
        // Happy Path
        @Test
        void shouldFindUserByEmail(){
            User userToFinder = TestHelper.userToFind();
            User userLoggingIn = TestHelper.userToFind();
            userToFinder.hashPassword();
            Result<LoggedInUser> expected = new Result<>();
            expected.setpayload(new LoggedInUser(userToFinder.getUserId(),userToFinder.getEmail(),1,JwtHandler.generateToken(userLoggingIn.getEmail())));
            when(collectionRepository.fetchUserCollection(userLoggingIn.getUserId())).thenReturn(TestHelper.collection());
            when(repository.findUserByEmail(userToFinder.getEmail())).thenReturn(userToFinder);
            Result<LoggedInUser> actual = service.findUserByEmail(userLoggingIn);
            assertTrue(actual.isSuccess());
            assertEquals(expected,actual);
        }

        @Test
        void shouldNotFindUserByEmailWhenCollectionDoesNotExist(){
            User userToFinder = TestHelper.userToFind();
            User userLoggingIn = TestHelper.userToFind();
            userToFinder.hashPassword();
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("No collection associated with user.",ResultType.NOT_FOUND);
            when(collectionRepository.fetchUserCollection(userLoggingIn.getUserId())).thenReturn(null);
            when(repository.findUserByEmail(userToFinder.getEmail())).thenReturn(userToFinder);
            Result<LoggedInUser> actual = service.findUserByEmail(userLoggingIn);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }

        // Email is blank/null
        @Test
        void shouldNotFindUserWithBlankEmail(){
            User userToFind = TestHelper.userToFind();
            userToFind.setEmail("");
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("Email cannot be blank.", ResultType.INVALID);
            expected.addErrorMessage("User does not exist.", ResultType.NOT_FOUND);
            Result<LoggedInUser> actual = service.findUserByEmail(userToFind);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }

        @Test
        void shouldNotFindUserWithNullEmail(){
            User userToFind = TestHelper.userToFind();
            userToFind.setEmail(null);
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("Email cannot be blank.", ResultType.INVALID);
            expected.addErrorMessage("Email cannot be null.", ResultType.INVALID);
            expected.addErrorMessage("User does not exist.", ResultType.NOT_FOUND);
            Result<LoggedInUser> actual = service.findUserByEmail(userToFind);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }

        // Null/blank password
        @Test
        void shouldNotFindUserWithBlankPassword(){
            User userToFind = TestHelper.userToFind();
            userToFind.setPassword("");
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("Password cannot be blank.", ResultType.INVALID);
            expected.addErrorMessage("User does not exist.", ResultType.NOT_FOUND);
            Result<LoggedInUser> actual = service.findUserByEmail(userToFind);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }

        @Test
        void shouldNotFindUserWithNullPassword(){
            User userToFind = TestHelper.userToFind();
            userToFind.setPassword(null);
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("Password cannot be null.", ResultType.INVALID);
            expected.addErrorMessage("Password cannot be blank.", ResultType.INVALID);
            expected.addErrorMessage("User does not exist.", ResultType.NOT_FOUND);
            Result<LoggedInUser> actual = service.findUserByEmail(userToFind);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }

        // Passwords don't match
        @Test
        void shouldNotFindUserWithMisMatchPassword(){
            User userToFinder = TestHelper.userToFind();
            User userLoggingIn = TestHelper.userToFind();
            userToFinder.setPassword("b");
            userToFinder.hashPassword();
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("Incorrect password.",ResultType.INVALID);
            when(repository.findUserByEmail(userToFinder.getEmail())).thenReturn(userToFinder );
            Result<LoggedInUser> actual = service.findUserByEmail(userLoggingIn);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }

        // User does not exist
        @Test
        void shouldNotFindNonExistentUser(){
            User userToFinder = TestHelper.userToFind();
            User userLoggingIn = TestHelper.userToFind();
            userLoggingIn.setEmail("IdontExist@email.com");
            userToFinder.hashPassword();
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("User does not exist.",ResultType.NOT_FOUND);
            when(repository.findUserByEmail(userToFinder.getEmail())).thenReturn(userToFinder );
            Result<LoggedInUser> actual = service.findUserByEmail(userLoggingIn);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }
    }

    @Nested
    class AddTests{
        // Happy path
        @Test
        void shouldAddUser(){
            User toAdd = TestHelper.userToAdd();
            Result<LoggedInUser> expected = new Result<>();
            LoggedInUser loggedInUser = TestHelper.loggedInUser();
            expected.setpayload(loggedInUser);
            when(collectionRepository.createCollection(toAdd.getUserId())).thenReturn(TestHelper.collection());
            when(repository.add(toAdd)).thenReturn(new User(3,"c@c.com","hashed"));
            Result<LoggedInUser> actual = service.add(toAdd);
            assertTrue(actual.isSuccess());
            assertEquals(actual.getpayload().id(), expected.getpayload().id());
        }

        // Null/Blank email
        @Test
        void shouldNotAddUserWithBlankEmail(){
            User toAdd = TestHelper.userToAdd();
            toAdd.setEmail("");
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("Email cannot be blank.", ResultType.INVALID);
            Result<LoggedInUser> actual = service.add(toAdd);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }

        @Test
        void shouldNotAddUserWithNullEmail(){
            User toAdd = TestHelper.userToAdd();
            toAdd.setEmail(null);
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("Email cannot be blank.", ResultType.INVALID);
            expected.addErrorMessage("Email cannot be null.", ResultType.INVALID);
            Result<LoggedInUser> actual = service.add(toAdd);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }

        // Null/blank password
        @Test
        void shouldNotAddUserWithBlankPassword(){
            User toAdd = TestHelper.userToAdd();
            toAdd.setPassword("");
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("Password cannot be blank.", ResultType.INVALID);
            Result<LoggedInUser> actual = service.add(toAdd);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }

        @Test
        void shouldNotAddUserWithNullPassword(){
            User toAdd = TestHelper.userToAdd();
            toAdd.setPassword(null);
            Result<LoggedInUser> expected = new Result<>();
            expected.addErrorMessage("Password cannot be blank.", ResultType.INVALID);
            expected.addErrorMessage("Password cannot be null.", ResultType.INVALID);
            Result<LoggedInUser> actual = service.add(toAdd);
            assertFalse(actual.isSuccess());
            assertEquals(expected,actual);
        }

        // Duplicate email
        @Test
        void shouldNotAddUserWithDuplicateEmail(){
            User toAdd = TestHelper.userToAdd();
            toAdd.setEmail("a@a.com");
            when(repository.add(toAdd)).thenThrow(DataIntegrityViolationException.class);
            Result<LoggedInUser> actual = service.add(toAdd);
            assertFalse(actual.isSuccess());
            assertTrue(actual.getErrorMessages().contains("Email already exists."));
            assertEquals(ResultType.INVALID, actual.getResultType());
        }
    }

}