package pl.edu.uam.restapi;

import com.wordnik.swagger.jaxrs.config.DefaultJaxrsConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@Order(value = 1)
@Configuration
public class MainApplicationInitializer implements ServletContextInitializer, WebApplicationInitializer{

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        registerInContext(rootContext);
        rootContext.setDisplayName(getApplicationName());
        rootContext.scan(getBaseScanPackages());

        servletContext.addListener(new RequestContextListener());

        initJersey(servletContext);
        initDefaultJaxrsConfig(servletContext);
        initBootstrap(servletContext);

        final FilterRegistration.Dynamic apiOriginFilter = servletContext.addFilter(
                "ApiOriginFilter",
                ApiOriginFilter.class
        );

        if (apiOriginFilter != null) {
            apiOriginFilter.addMappingForUrlPatterns(null, false, "/api/*");
        }
    }

    private void registerInContext(AnnotationConfigWebApplicationContext rootContext) {
        rootContext.register(getClass());
    }

    private String[] getBaseScanPackages() {
        return new String[]{
                "pl.edu.uam.restapi"
        };
    }

    private String getApplicationName() {
        return "restapi-demo";
    }

    private String getApplicationClassName() {
        return Application.class.getName();
    }

    protected void initJersey(ServletContext servletContext) {

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("jersey", new ServletContainer());

        dispatcher.setInitParameter("javax.ws.rs.Application", getApplicationClassName());
        dispatcher.setInitParameter(ServerProperties.WADL_FEATURE_DISABLE, "true");
        dispatcher.setInitParameter(ServerProperties.APPLICATION_NAME, getApplicationName());

        dispatcher.addMapping("/api/*");
        dispatcher.setLoadOnStartup(1);
    }

    protected void initDefaultJaxrsConfig(ServletContext servletContext) {

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DefaultJaxrsConfig", DefaultJaxrsConfig.class.getName());

        dispatcher.setInitParameter("api.version", "1.0.0");
        dispatcher.setInitParameter("swagger.api.basepath", "http://localhost:8080/api");
        dispatcher.setLoadOnStartup(2);
    }

    protected void initBootstrap(ServletContext servletContext) {
        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SwaggerBootstrap", Bootstrap.class.getName());
        dispatcher.setLoadOnStartup(2);
    }
}
