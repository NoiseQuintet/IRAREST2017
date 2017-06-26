package pl.edu.uam.restapi.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import pl.edu.uam.restapi.database.BurgerDatabase;
import pl.edu.uam.restapi.exceptions.UserException;
import pl.edu.uam.restapi.model.Burger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

public abstract class AbstractBurgersResource {

    private BurgerDatabase burgerDatabase;

    public AbstractBurgersResource(BurgerDatabase burgerDatabase) {
        this.burgerDatabase = burgerDatabase;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get burgers collection", notes = "Get burgers collection", response = Burger.class, responseContainer = "LIST")
    public Collection<Burger> list() {
        return burgerDatabase.getBurgers();
    }


    @Path("/{burgerId}")
    @ApiOperation(value = "Get burger by id", notes = "[note]Get burger by id", response = Burger.class)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Burger getBurger(@PathParam("burgerId") String burgerId) throws Exception {
        Burger burger = burgerDatabase.getBurger(burgerId);

        if (burgerId.equals("db")) {
            throw new Exception("Database error");
        }

        if (burger == null) {
            throw new UserException("Burger not found", "Burger nie został znaleziony", "http://docu.pl/errors/burger-not-found");
        }

        return burger;
    }

    @POST
    @ApiOperation(value = "Create burger", notes = "Create burger", response = Burger.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBurger(Burger burger) {
        Burger dbBurger = new Burger(
                "",
                burger.getName(),
                burger.getPrice(),
                burger.getIngredients()
        );

        Burger createdBurger = burgerDatabase.createBurger(dbBurger);

        return Response.created(URI.create(uriInfo.getPath() + "/" + createdBurger.getId())).entity(createdBurger).build();
    }

    @PUT
    @Path("/{burgerId}")
    @ApiOperation(value = "Update burger", notes = "Update burger", response = Burger.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response udpateburger(@PathParam("burgerId") String burgerId, Burger burger) {
        Burger burgerFromDb = burgerDatabase.getBurger(burgerId);

        if (burgerFromDb == null) {
            throw new UserException("Burger not found", "Burger nie został znaleziony", "http://docu.pl/errors/burger-not-found");
        }

        Burger udpatedBurger = burgerDatabase.updateBurger(burgerId, burger);

        return Response.ok(udpatedBurger).build();
    }

    @Path("/{burgerId}")
    @ApiOperation(value = "Delete burger", notes = "Delete burger", response = Burger.class)
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteBurger(@PathParam("burgerId") String burgerId) throws Exception {
        Burger burger = burgerDatabase.getBurger(burgerId);

        if (burgerId.equals("db")) {
            throw new Exception("Database error");
        }

        if (burger == null) {
            throw new UserException("Burger not found", "Burger nie został znaleziony", "http://docu.pl/errors/burger-not-found");
        }

        burgerDatabase.deleteBurger(burgerId);

    }
}
