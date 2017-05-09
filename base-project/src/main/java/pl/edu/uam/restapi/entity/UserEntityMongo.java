package pl.edu.uam.restapi.entity;

import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.PostLoad;
import org.mongodb.morphia.annotations.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@org.mongodb.morphia.annotations.Entity("users")
public class UserEntityMongo {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEntityMongo.class);

    // auto-generated, if not set (see ObjectId)
    @Id
    ObjectId id;

    //fields can be renamed
    @Property("first")
    private String firstName;

    //fields can be renamed
    @Property("last")
    private String lastName;

    //fields can be indexed for better performance
    @Indexed
    private boolean active = false;

    //Lifecycle methods -- Pre/PostLoad, Pre/PostPersist...
    @PostLoad
    private void postLoad(DBObject dbObj) {
        LOGGER.info("postLoad: {}", dbObj);
    }

    public UserEntityMongo() {
    }

    public UserEntityMongo(String firstName, String lastName, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }

    public ObjectId getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
    }
}
