package pl.edu.uam.restapi.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import pl.edu.uam.restapi.database.OrderDatabase;
import pl.edu.uam.restapi.exceptions.UserException;
import pl.edu.uam.restapi.model.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

public abstract class AbstractOrdersResource {

    private OrderDatabase orderDatabase;

    public AbstractOrdersResource(OrderDatabase orderDatabase) {
        this.orderDatabase = orderDatabase;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get orders collection", notes = "Get orders collection", response = Order.class, responseContainer = "LIST")
    public Collection<Order> list() {
        return orderDatabase.getOrders();
    }


    @Path("/{orderId}")
    @ApiOperation(value = "Get order by id", notes = "[note]Get order by id", response = Order.class)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Order getOrder(@PathParam("orderId") String orderId) throws Exception {
        Order order = orderDatabase.getOrder(orderId);

        if (orderId.equals("db")) {
            throw new Exception("Database error");
        }

        if (order == null) {
            throw new UserException("Order not found", "Order nie został znaleziony", "http://docu.pl/errors/order-not-found");
        }

        return order;
    }

    @POST
    @ApiOperation(value = "Create order", notes = "Create order", response = Order.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(Order order) {
        Order dbOrder = new Order(
                "",
                order.getUser(),
                order.getBurger(),
                order.getDate()
        );

        Order createdOrder = orderDatabase.createOrder(dbOrder);

        return Response.created(URI.create(uriInfo.getPath() + "/" + createdOrder.getId())).entity(createdOrder).build();
    }

    @PUT
    @Path("/{orderId}")
    @ApiOperation(value = "Update order", notes = "Update order", response = Order.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response udpateorder(@PathParam("orderId") String orderId, Order order) {
        Order orderFromDb = orderDatabase.getOrder(orderId);

        if (orderFromDb == null) {
            throw new UserException("Order not found", "Order nie został znaleziony", "http://docu.pl/errors/order-not-found");
        }

        Order udpatedOrder = orderDatabase.updateOrder(orderId, order);

        return Response.ok(udpatedOrder).build();
    }

    @Path("/{orderId}")
    @ApiOperation(value = "Delete order", notes = "Delete order", response = Order.class)
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteOrder(@PathParam("orderId") String orderId) throws Exception {
        Order user = orderDatabase.getOrder(orderId);

        if (orderId.equals("db")) {
            throw new Exception("Database error");
        }

        if (user == null) {
            throw new UserException("Order not found", "Order nie został znaleziony", "http://docu.pl/errors/user-not-found");
        }

        orderDatabase.deleteOrder(orderId);

    }
}
