package mtgcollection.data;

import mtgcollection.model.User;

public interface UserRepository {
    // Fetch user
    User findUserByEmail(String email);

    // Create a new user
    User add(User user);

}
