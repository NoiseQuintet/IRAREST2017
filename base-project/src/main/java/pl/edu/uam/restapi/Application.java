package pl.edu.uam.restapi;

import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider;
import com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider;
import org.glassfish.jersey.filter.*;
import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {

  public Application() {
    packages("pl.edu.uam.restapi").
    register(LoggingFilter.class).
    register(ApiListingResourceJSON.class).
    register(JerseyApiDeclarationProvider.class).
    register(JerseyResourceListingProvider.class);
  }
}
