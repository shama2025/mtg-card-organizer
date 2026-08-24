package mtgcollection;


import mtgcollection.dto.LoggedInUser;
import mtgcollection.model.User;

import java.util.List;

public class TestHelper {

    public static User userToFind(){
        return new User(1,"a@a.com","a");
    }

    public static User userToAdd(){
        return new User(0,"c@c.com","c");
    }

    public static LoggedInUser loggedInUser(){return new LoggedInUser(1,"a@a.com");}

}
