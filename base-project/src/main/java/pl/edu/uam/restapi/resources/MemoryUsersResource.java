package pl.edu.uam.restapi.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.database.MemoryDB;

import javax.ws.rs.Path;

@Path("/users")
@Api(value = "/users", description = "Operations about users using static java array")
public class MemoryUsersResource extends AbstractUsersResource {

    public MemoryUsersResource() {
        super(new MemoryDB());
    }
}
