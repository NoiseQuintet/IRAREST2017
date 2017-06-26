package pl.edu.uam.restapi.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import pl.edu.uam.restapi.entity.BurgerEntity;

import java.util.List;

@ApiModel(value = "Burger")
public class Burger {
    private String id;
    private String name;
    private int price;
    private List<String> ingredients;

    public Burger() {
    }

    public Burger(String id, String name, int price, List<String> ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients=ingredients;
    }

    @ApiModelProperty(value = "Burger id", required = true)
    public String getId() {
        return id;
    }

    @ApiModelProperty(value = "Burger name", required = true)
    public String getName() {
        return name;
    }

    @ApiModelProperty(value = "Burger price", required = true)
    public int getPrice() {
        return price;
    }
    @ApiModelProperty(value = "Burger ingredients", required = false)
    public List<String> getIngredients() {
        return ingredients;
    }

    public BurgerEntity toBurgerEntity(boolean active){
        return new BurgerEntity(Long.parseLong(id),name,price,ingredients,active);
    }

}
