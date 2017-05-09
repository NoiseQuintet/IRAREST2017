package pl.edu.uam.restapi.database;

import pl.edu.uam.restapi.model.User;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisDB implements UserDatabase {

    private static final String HOST = "dab.redistogo.com";
    private static final int PORT = 9498;
    private static final String PASSWORD = "05615f45a0fe99d5c91340e13c68432d";
    private static final String USERS_COLLECTION = "users_collection";

    private final String[] USER_FIELDS = {"id", "firstName", "lastName", "active"};

    private static JedisPool datastore;

    public static JedisPool getJedisPool() {
        if (datastore == null) {
            datastore = new JedisPool(
                    new JedisPoolConfig(),
                    HOST,
                    PORT,
                    Protocol.DEFAULT_TIMEOUT,
                    PASSWORD
            );

        }

        return datastore;
    }

    private static Jedis getJedisResource() {
        return getJedisPool().getResource();
    }

    @Override
    public User getUser(String id) {
        Jedis jedisResource = getJedisResource();

        try {
            Boolean exists = jedisResource.hexists(id, "id");
            if (exists) {
                List<String> userFields = jedisResource.hmget(id, USER_FIELDS);
                return buildUserResponse(userFields);
            }

            return null;

        } finally {
            getJedisPool().returnResource(jedisResource);
        }
    }

    @Override
    public User createUser(final User user) {
        Jedis jedisResource = getJedisResource();

        try {
            final String id = String.valueOf(jedisResource.scard(USERS_COLLECTION) + 1);

            Map<String, String> userMap = new HashMap<String, String>() {{
                put("id", id);
                put("firstName", user.getFirstName());
                put("lastName", user.getLastName());
                put("active", "false");
            }};

            jedisResource.hmset(id, userMap);
            jedisResource.sadd(USERS_COLLECTION, id);

            return buildUserResponse(user, id);

        } finally {
            getJedisPool().returnResource(jedisResource);
        }
    }

    @Override
    public Collection<User> getUsers() {
        Jedis jedisResource = getJedisResource();

        try {
            Collection<User> lists = new ArrayList<User>();
            Set<String> ids = jedisResource.smembers(USERS_COLLECTION);

            for (String id : ids) {
                List<String> userFields = jedisResource.hmget(id, USER_FIELDS);
                lists.add(buildUserResponse(userFields));
            }

            return lists;

        } finally {
            getJedisPool().returnResource(jedisResource);
        }
    }

    @Override
    public User updateUser(String userId, User user) {
        return null;
    }

    private User buildUserResponse(User user, Object id) {
        return new User(id.toString(), user.getFirstName(), user.getLastName());
    }

    private User buildUserResponse(List<String> list) {
        return new User(list.get(0), list.get(1), list.get(2));
    }
}
