package pl.edu.uam.restapi;

import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.model.AuthorizationScope;
import com.wordnik.swagger.model.AuthorizationType;
import com.wordnik.swagger.model.GrantType;
import com.wordnik.swagger.model.OAuthBuilder;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

public class Bootstrap extends HttpServlet {
    static {
        // do any additional initialization here, such as set your base path programmatically as such:
        // ConfigFactory.config().setBasePath("http://www.foo.com/");

        // add a custom filter
//    FilterFactory.setFilter(new CustomFilter());

        ApiInfo info = new ApiInfo(
                "Burger Express API",                             /* title */
                " REST 2017 Project ",
                "",                  /* TOS URL */
                "mc35419@st.amu.edu.pl",                            /* Contact */
                "Apache 2.0",                                     /* license */
                "http://www.apache.org/licenses/LICENSE-2.0.html" /* license URL */
        );

        List<AuthorizationScope> scopes = new ArrayList<AuthorizationScope>();

        List<GrantType> grantTypes = new ArrayList<GrantType>();

        AuthorizationType oauth = new OAuthBuilder().scopes(scopes).grantTypes(grantTypes).build();

        ConfigFactory.config().addAuthorization(oauth);
        ConfigFactory.config().setApiInfo(info);
    }
}
