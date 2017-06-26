package pl.edu.uam.restapi.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.database.PostgresqlDBBurger;

import javax.ws.rs.Path;

@Path("/postgresql/burgers")
@Api(value = "/postgresql/burgers", description = "Operations about burgers using postgresql")
public class PostgresBurgersResource extends AbstractBurgersResource {

    public PostgresBurgersResource() {
        super(new PostgresqlDBBurger());
    }
}
