package pl.edu.uam.restapi.database;

import com.google.common.collect.Lists;
import pl.edu.uam.restapi.entity.BurgerEntity;
import pl.edu.uam.restapi.exceptions.UserException;
import pl.edu.uam.restapi.model.Burger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.*;

public class PostgresqlDBBurger implements BurgerDatabase {

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
    public Burger getBurger(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return null;
        }

        BurgerEntity burgerEntity = getEntityManager()
                .find(BurgerEntity.class, id);

        if (burgerEntity != null) {
            return buildBurgerResponse(burgerEntity);
        }

        return null;
    }

    @Override
    public void deleteBurger(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return ;
        }

        BurgerEntity burgerEntity = getEntityManager()
                .find(BurgerEntity.class, id);

        if (burgerEntity == null) {
            throw new UserException("nie","ma","takiego zamówienia");
        }
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(burgerEntity);
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
    public Burger createBurger(final Burger burger) {
        BurgerEntity entity = buildBurgerEntity(burger, false);

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

        return new Burger(String.valueOf(entity.getId()), entity.getName(), entity.getPrice(),entity.getIngredients());
    }

    @Override
    public Collection<Burger> getBurgers() {
        Query query = getEntityManager().createNamedQuery("burgers.findAll");
        List<BurgerEntity> resultList = query.getResultList();

        List<Burger> list = Collections.emptyList();

        if (resultList != null && !resultList.isEmpty()) {
            list = Lists.newArrayListWithCapacity(resultList.size());

            for (BurgerEntity burger : resultList) {
                list.add(buildBurgerResponse(burger));
            }
        }

        return list;
    }

    @Override
    public Burger updateBurger(String userId, Burger user) {
        Long id = null;

        try {
            id = Long.valueOf(userId);
        } catch (NumberFormatException e) {
            return null;
        }
        BurgerEntity entity = buildFullBurgerEntity(user, false);
        // BurgerEntity userEntity = getEntityManager()
        //        .find(BurgerEntity.class, id);

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
        return new Burger(String.valueOf(entity.getId()), entity.getName(), entity.getPrice(),entity.getIngredients());
    }

    private Burger buildBurgerResponse(BurgerEntity burgerEntity) {
        return new Burger(burgerEntity.getId().toString(), burgerEntity.getName(), burgerEntity.getPrice(),burgerEntity.getIngredients());
    }

    private BurgerEntity buildBurgerEntity(Burger burger, boolean active) {
        return new BurgerEntity(burger.getName(), burger.getPrice(),burger.getIngredients(), active);
    }

    private BurgerEntity buildFullBurgerEntity(Burger burger, boolean active) {
        return new BurgerEntity(Long.parseLong(burger.getId()),burger.getName(), burger.getPrice(),burger.getIngredients(), active);
    }

}
