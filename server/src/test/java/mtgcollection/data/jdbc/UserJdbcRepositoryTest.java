package mtgcollection.data.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import mtgcollection.TestHelper;
import mtgcollection.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserJdbcRepositoryTest {

    @Autowired
    UserJdbcRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void runSetKnownGoodState(){
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Nested
    class FindUserByEmailTest{
        // Happy Path
        @Test
        void shouldFindUserByEmail(){
            User expected = TestHelper.userToFind();
            User actual = repository.findUserByEmail("a@a.com");
            assertEquals(expected,actual);
        }

        // Not found user
        @Test
        void shouldNotFindUserByEmail(){
            User missingUser = repository.findUserByEmail("idontexist@email.com");
            assertNull(missingUser);
        }
    }

    @Nested
    class AddTests{
        @Test
        void shouldAdd(){
            User userToAdd = TestHelper.userToAdd();
            User expected = TestHelper.userToAdd();
            expected.setUserId(3);
            User actual = repository.add(userToAdd);
            assertEquals(expected,actual);
        }

        @Test
        void shouldNotAddUserWithNullEmail()throws DataIntegrityViolationException {
            User userToAdd = TestHelper.userToAdd();
            userToAdd.setEmail(null);
            assertThrows(DataIntegrityViolationException.class, ()->{
               repository.add(userToAdd);
            });
        }

        @Test
        void shouldNotAddUserWithNullPassword(){
            User userToAdd = TestHelper.userToAdd();
            userToAdd.setPassword(null);
            assertThrows(DataIntegrityViolationException.class, ()->{
                repository.add(userToAdd);
            });
        }

        @Test
        void shouldNotAddUserWithDuplicateEmail(){
            User userToAdd = TestHelper.userToAdd();
            userToAdd.setEmail("a@a.com");
            assertThrows(DataIntegrityViolationException.class, ()->{
                repository.add(userToAdd);
            });
        }
    }
}