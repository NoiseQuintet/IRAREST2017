package pl.edu.uam.restapi.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.database.RedisDB;

import javax.ws.rs.Path;

@Path("/redis/users")
@Api(value = "/redis/users", description = "Operations about users using redisDB")
public class RedisUsersResource extends AbstractUsersResource {

    public RedisUsersResource() {
        super(new RedisDB());
    }

}
