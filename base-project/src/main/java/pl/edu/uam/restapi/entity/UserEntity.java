package pl.edu.uam.restapi.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "users.findAll", query = "SELECT u FROM UserEntity u")
})
public class UserEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEntity.class);

    // auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //fields can be renamed
    @Column(name = "first")
    private String firstName;

    //fields can be renamed
    @Column(name = "last")
    private String lastName;

    //fields can be indexed for better performance
    private boolean active = false;

    //Lifecycle methods -- Pre/PostLoad, Pre/PostPersist...
    @PostLoad
    private void postLoad() {
        LOGGER.info("postLoad: {}", toString());
    }

    public UserEntity() {
    }

    public UserEntity(String firstName, String lastName, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }

    public UserEntity(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("active", active)
                .toString();
    }
}
