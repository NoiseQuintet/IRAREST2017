package pl.edu.uam.restapi.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.database.MysqlDB;

import javax.ws.rs.Path;

@Path("/mysql/users")
@Api(value = "/mysql/users", description = "Operations about users using mysql")
public class MysqlUsersResource extends AbstractUsersResource {

    public MysqlUsersResource() {
        super(new MysqlDB());
    }
}
