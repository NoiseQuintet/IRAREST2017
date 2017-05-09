package pl.edu.uam.restapi.database;

import com.google.common.collect.Lists;
import pl.edu.uam.restapi.entity.UserEntity;
import pl.edu.uam.restapi.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlDB implements UserDatabase {

    private static final String HOST = "173.194.242.162";
    private static final int PORT = 3306;
    private static final String DATABASE = "uamtest01";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";

    private static EntityManager entityManager;

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            String dbUrl = "jdbc:mysql://" + HOST + ':' + PORT + "/" + DATABASE;
            Map<String, String> properties = new HashMap<String, String>();

            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
            properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
            properties.put("hibernate.connection.url", dbUrl);
            properties.put("hibernate.connection.username", USER_NAME);
            properties.put("hibernate.connection.password", PASSWORD);
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.format_sql", "true");

            properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false"); //PERFORMANCE TIP!
            properties.put("hibernate.hbm2ddl.auto", "update"); //update schema for entities (create tables if not exists)

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("myUnit", properties);
            entityManager = emf.createEntityManager();
        }

        return entityManager;
    }

    @Override
    public User getUser(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return null;
        }

        UserEntity userEntity = getEntityManager()
                .find(UserEntity.class, id);

        if (userEntity != null) {
            return buildUserResponse(userEntity);
        }

        return null;
    }

    @Override
    public User createUser(final User user) {
        UserEntity entity = buildUserEntity(user, false);

        try {
            getEntityManager().getTransaction().begin();

            // Operations that modify the database should come here.
            getEntityManager().persist(entity);

            getEntityManager().getTransaction().commit();
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }

        return new User(String.valueOf(entity.getId()), entity.getFirstName(), entity.getLastName());
    }

    @Override
    public Collection<User> getUsers() {
        Query query = getEntityManager().createNamedQuery("users.findAll");
        List<UserEntity> resultList = query.getResultList();

        List<User> list = Collections.emptyList();

        if (resultList != null && !resultList.isEmpty()) {
            list = Lists.newArrayListWithCapacity(resultList.size());

            for (UserEntity user : resultList) {
                list.add(buildUserResponse(user));
            }
        }

        return list;
    }

    @Override
    public User updateUser(String userId, User user) {

        Long id = null;

        try {
            id = Long.valueOf(userId);
        } catch (NumberFormatException e) {
            return null;
        }

        UserEntity userEntity = getEntityManager()
                .find(UserEntity.class, id);

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        getEntityManager().persist(userEntity);

        return buildUserResponse(userEntity);
    }

    private User buildUserResponse(UserEntity userEntity) {
        return new User(userEntity.getId().toString(), userEntity.getFirstName(), userEntity.getLastName());
    }

    private UserEntity buildUserEntity(User user, boolean active) {
        return new UserEntity(user.getFirstName(), user.getLastName(), active);
    }
}
