package pl.edu.uam.restapi.database;

import pl.edu.uam.restapi.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryDB implements UserDatabase {
    private static Map<String, User> users = new HashMap<String, User>() {{
        put("0", new User("0", "Mieszko", "Pierwszy"));
        put("1", new User("1", "Bolesław", "Chrobry"));
        put("2", new User("1", "Kazimierz", "Wielki"));
    }};

    @Override
    public User getUser(String id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        String id = Integer.toString(users.size());
        User value = new User(id, user.getFirstName(), user.getLastName());
        users.put(id, value);
        return value;
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User updateUser(String userId, User user) {
        return null;
    }
}
