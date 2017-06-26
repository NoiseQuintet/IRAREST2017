package pl.edu.uam.restapi.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import pl.edu.uam.restapi.entity.UserEntity;

@ApiModel(value = "User")
public class User {
    private String id;
    private String firstName;
    private String lastName;

    public User() {
    }

    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @ApiModelProperty(value = "User id", required = true)
    public String getId() {
        return id;
    }

    @ApiModelProperty(value = "User first name", required = true)
    public String getFirstName() {
        return firstName;
    }

    @ApiModelProperty(value = "User last name", required = true)
    public String getLastName() {
        return lastName;
    }

    public UserEntity toUserEntity(boolean active){
        return new UserEntity(Long.parseLong(id),firstName,lastName,active);
    }
}
