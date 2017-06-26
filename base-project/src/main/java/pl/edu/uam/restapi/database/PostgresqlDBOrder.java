package pl.edu.uam.restapi.database;

import com.google.common.collect.Lists;
import pl.edu.uam.restapi.entity.OrderEntity;
import pl.edu.uam.restapi.exceptions.UserException;
import pl.edu.uam.restapi.model.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.*;

public class PostgresqlDBOrder implements OrderDatabase {

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
    public Order getOrder(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return null;
        }

        OrderEntity orderEntity = getEntityManager()
                .find(OrderEntity.class, id);

        if (orderEntity != null) {
            return buildOrderResponse(orderEntity);
        }

        return null;
    }

    @Override
    public void deleteOrder(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return ;
        }

        OrderEntity orderEntity = getEntityManager()
                .find(OrderEntity.class, id);

        if (orderEntity == null) {
            throw new UserException("nie","ma","takiego zamówienia");
        }
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(orderEntity);
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
    public Order createOrder(final Order order) {
        OrderEntity entity = buildOrderEntity(order, false);

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

        return new Order(String.valueOf(entity.getId()),entity.getUser().toUser(), entity.getBurger().toBurger(),  entity.getDate());
    }

    @Override
    public Collection<Order> getOrders() {
        Query query = getEntityManager().createNamedQuery("orders.findAll");
        List<OrderEntity> resultList = query.getResultList();

        List<Order> list = Collections.emptyList();

        if (resultList != null && !resultList.isEmpty()) {
            list = Lists.newArrayListWithCapacity(resultList.size());

            for (OrderEntity order : resultList) {
                list.add(buildOrderResponse(order));
            }
        }

        return list;
    }

    @Override
    public Order updateOrder(String userId, Order user) {
        Long id = null;

        try {
            id = Long.valueOf(userId);
        } catch (NumberFormatException e) {
            return null;
        }
        OrderEntity entity = buildFullOrderEntity(user, false);
        // OrderEntity userEntity = getEntityManager()
        //        .find(OrderEntity.class, id);

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
        return new Order(String.valueOf(entity.getId()), entity.getUser().toUser(), entity.getBurger().toBurger(),entity.getDate());
    }

    private Order buildOrderResponse(OrderEntity orderEntity) {
        return new Order(orderEntity.getId().toString(), orderEntity.getUser().toUser(), orderEntity.getBurger().toBurger(),orderEntity.getDate());
    }

    private OrderEntity buildOrderEntity(Order order, boolean active) {
        return new OrderEntity( order.getBurger().toBurgerEntity(active),order.getUser().toUserEntity(active),order.getDate(), active);
    }

    private OrderEntity buildFullOrderEntity(Order order, boolean active) {
        return new OrderEntity( Long.parseLong(order.getId()),order.getBurger().toBurgerEntity(active),order.getUser().toUserEntity(active),order.getDate(), active);
    }
}
