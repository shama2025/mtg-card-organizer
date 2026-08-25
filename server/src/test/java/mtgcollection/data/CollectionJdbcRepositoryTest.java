package mtgcollection.data;

import mtgcollection.TestHelper;
import mtgcollection.model.Collection;
import mtgcollection.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CollectionJdbcRepositoryTest {

    @Autowired
    JdbcClient jdbcClient;

    @Autowired
    CollectionJdbcRepository collectionRepository;

    @Autowired
    UserJdbcRepository userRepository;

    @BeforeEach
    void runSetKnownGoodState(){
        jdbcClient.sql("call set_known_good_state();").update();
    }


    @Nested
    class CreateCollectionTest{
        // Should be able to create a collection
        @Test
        void shouldCreateCollection(){
            User newUser = userRepository.add(TestHelper.userToAdd());
            Collection collection = collectionRepository.createCollection(newUser.getUserId());
            assertEquals(3,collection.getOwner());
        }
        // Should not create a new collection when user already has a collection
        @Test
        void shouldNotCreateCollectionForExistingUser(){
            assertThrows(DuplicateKeyException.class, () -> collectionRepository.createCollection(1));
        }

    }

}