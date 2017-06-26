package pl.edu.uam.restapi.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.uam.restapi.model.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "users.findAll", query = "SELECT u FROM UserEntity u")
})
public class UserEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEntity.class);

    // auto-generated
    @Id
    @Column(name="userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //fields can be renamed
    @Column(name = "first")
    private String firstName;

    //fields can be renamed
    @Column(name = "last")
    private String lastName;

    @OneToMany(mappedBy = "user")
    private List<OrderEntity> orders;

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
    public UserEntity(Long id, String firstName, String lastName, boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
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

    public User toUser(){
        return new User(id.toString(),firstName,lastName);
    }
}
