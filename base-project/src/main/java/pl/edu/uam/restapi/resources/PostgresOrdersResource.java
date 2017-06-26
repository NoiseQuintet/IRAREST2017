package pl.edu.uam.restapi.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.database.PostgresqlDBOrder;

import javax.ws.rs.Path;

@Path("/postgresql/orders")
@Api(value = "/postgresql/orders", description = "Operations about orders using postgresql")
public class PostgresOrdersResource extends AbstractOrdersResource {

    public PostgresOrdersResource() {
        super(new PostgresqlDBOrder());
    }
}
