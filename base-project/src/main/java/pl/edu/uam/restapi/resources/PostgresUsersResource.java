package pl.edu.uam.restapi.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.database.PostgresqlDB;

import javax.ws.rs.Path;

@Path("/postgresql/users")
@Api(value = "/postgresql/users", description = "Operations about users using postgresql")
public class PostgresUsersResource extends AbstractUsersResource {

    public PostgresUsersResource() {
        super(new PostgresqlDB());
    }
}
