package pl.edu.uam.restapi.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import pl.edu.uam.restapi.database.UserDatabase;
import pl.edu.uam.restapi.exceptions.UserException;
import pl.edu.uam.restapi.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

public abstract class AbstractUsersResource {

    private UserDatabase userDatabase;

    public AbstractUsersResource(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get users collection", notes = "Get users collection", response = User.class, responseContainer = "LIST")
    public Collection<User> list() {
        return userDatabase.getUsers();
    }


    @Path("/{userId}")
    @ApiOperation(value = "Get user by id", notes = "[note]Get user by id", response = User.class)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("userId") String userId) throws Exception {
        User user = userDatabase.getUser(userId);

        if (userId.equals("db")) {
            throw new Exception("Database error");
        }

        if (user == null) {
            throw new UserException("User not found", "Użytkownik nie został znaleziony", "http://docu.pl/errors/user-not-found");
        }

        return user;
    }

    @POST
    @ApiOperation(value = "Create user", notes = "Create user", response = User.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        User dbUser = new User(
                "",
                user.getFirstName(),
                user.getLastName()
        );

        User createdUser = userDatabase.createUser(dbUser);

        return Response.created(URI.create(uriInfo.getPath() + "/" + createdUser.getId())).entity(createdUser).build();
    }

    @PUT
    @Path("/{userId}")
    @ApiOperation(value = "Update user", notes = "Update user", response = User.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response udpateuser(@PathParam("userId") String userId, User user) {
        User userFromDb = userDatabase.getUser(userId);

        if (userFromDb == null) {
            throw new UserException("User not found", "Użytkownik nie został znaleziony", "http://docu.pl/errors/user-not-found");
        }

        User udpatedUser = userDatabase.updateUser(userId, user);

        return Response.ok(udpatedUser).build();
    }


    @Path("/{userId}")
    @ApiOperation(value = "Delete user", notes = "Delete user", response = User.class)
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteUser(@PathParam("userId") String userId) throws Exception {
        User user = userDatabase.getUser(userId);

        if (userId.equals("db")) {
            throw new Exception("Database error");
        }

        if (user == null) {
            throw new UserException("User not found", "Użytkownik nie został znaleziony", "http://docu.pl/errors/user-not-found");
        }

        userDatabase.deleteUser(userId);

    }
}
