package pl.edu.uam.restapi.database;

import pl.edu.uam.restapi.model.User;

import java.util.Collection;

public interface UserDatabase {
    User getUser(String id);

    User createUser(User user);

    Collection<User> getUsers();

    User updateUser(String userId, User user);
}
