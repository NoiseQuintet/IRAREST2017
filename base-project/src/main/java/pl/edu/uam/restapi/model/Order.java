package pl.edu.uam.restapi.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Order")
public class Order {
    private String id;
    private Burger burger;
    private User user;
    private String date;

    public Order() {
    }

    public Order(String id, User user, Burger burger,String date) {
        this.id = id;
        this.burger = burger;
        this.user = user;
        this.date=date;
    }

    @ApiModelProperty(value = "Order id", required = true)
    public String getId() {
        return id;
    }

    @ApiModelProperty(value = "Order burger", required = true)
    public Burger getBurger() {
        return burger;
    }

    @ApiModelProperty(value = "Order user", required = true)
    public User getUser() {
        return user;
    }
    @ApiModelProperty(value = "Order date", required = false)
    public String getDate() {
        return date;
    }
}
