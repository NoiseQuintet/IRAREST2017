package pl.edu.uam.restapi.database;

import pl.edu.uam.restapi.model.Order;

import java.util.Collection;

public interface OrderDatabase {
    Order getOrder(String id);

    Order createOrder(Order order);

    Collection<Order> getOrders();

    Order updateOrder(String userId, Order order);

    void deleteOrder(String id);
}
