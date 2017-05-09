package pl.edu.uam.restapi.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.database.MongoDB;

import javax.ws.rs.Path;

@Path("/mongo/users")
@Api(value = "/mongo/users", description = "Operations about users using mongoDB")
public class MongoUsersResource extends AbstractUsersResource {

    public MongoUsersResource() {
        super(new MongoDB());
    }
}
