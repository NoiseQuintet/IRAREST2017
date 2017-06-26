package pl.edu.uam.restapi.database;

import com.google.common.collect.Lists;
import pl.edu.uam.restapi.entity.UserEntity;
import pl.edu.uam.restapi.exceptions.UserException;
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

public class PostgresqlDBUser implements UserDatabase {

    private static final String HOST = "fizzy-cherry.db.elephantsql.com";
    private static final int PORT = 5432;
    private static final String DATABASE = "svaafmpd";
    private static final String USER_NAME = "svaafmpd";
    private static final String PASSWORD = "OwNEWxVlpdoXimyCXnY6VgjMAR-c8Kn1";

    private static EntityManager entityManager;

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            String dbUrl = "jdbc:postgresql://" + HOST + ':' + PORT + "/" + DATABASE;

            Map<String, String> properties = new HashMap<String, String>();

            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
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
    public void deleteUser(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return ;
        }

        UserEntity userEntity = getEntityManager()
                .find(UserEntity.class, id);

        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(userEntity);
            getEntityManager().getTransaction().commit();
        } finally {
            {if (getEntityManager().getTransaction().isActive()){
                getEntityManager().getTransaction().rollback();
            }
        }
        }
        return;
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
        UserEntity entity = buildFullUserEntity(user, false);
       // UserEntity userEntity = getEntityManager()
        //        .find(UserEntity.class, id);

        try {
            getEntityManager().getTransaction().begin();

            getEntityManager().merge(entity);
            getEntityManager().getTransaction().commit();
        } finally {
            {if (getEntityManager().getTransaction().isActive()){
                getEntityManager().getTransaction().rollback();
            }
            }
        }
        return new User(String.valueOf(entity.getId()), entity.getFirstName(), entity.getLastName());
    }

    private User buildUserResponse(UserEntity userEntity) {
        return new User(userEntity.getId().toString(), userEntity.getFirstName(), userEntity.getLastName());
    }

    private UserEntity buildUserEntity(User user, boolean active) {
        return new UserEntity(user.getFirstName(), user.getLastName(), active);
    }

    private UserEntity buildFullUserEntity(User user, boolean active) {
        return new UserEntity(Long.parseLong(user.getId()),user.getFirstName(), user.getLastName(), active);
    }
}
