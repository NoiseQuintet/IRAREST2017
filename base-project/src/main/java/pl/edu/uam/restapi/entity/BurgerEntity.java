package pl.edu.uam.restapi.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.uam.restapi.model.Burger;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "burgers")
@NamedQueries({
        @NamedQuery(name = "burgers.findAll", query = "SELECT u FROM BurgerEntity u")
})
public class BurgerEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(BurgerEntity.class);

    // auto-generated
    @Id
    @Column(name="burgerId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //fields can be renamed
    @Column(name = "name")
    private String name;

    //fields can be renamed
    @Column(name = "price")
    private int price;

    //fields can be renamed
    @Column(name = "ingredients")
    @ElementCollection
    private List<String>ingredients;

    @OneToMany(mappedBy = "burger")
    private List<OrderEntity> orders;

    //fields can be indexed for better performance
    private boolean active = false;

    //Lifecycle methods -- Pre/PostLoad, Pre/PostPersist...
    @PostLoad
    private void postLoad() {
        LOGGER.info("postLoad: {}", toString());
    }

    public BurgerEntity() {
    }

    public BurgerEntity(String name, int price,List<String> ingredients, boolean active) {
        this.name = name;
        this.price = price;
        this.ingredients=ingredients;
        this.active = active;
    }

    public BurgerEntity(Long id, String name, int price, List<String> ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients=ingredients;
    }

    public BurgerEntity(Long id, String name, int price, List<String> ingredients,boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients=ingredients;
        this.active=active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getIngredients(){
        return ingredients;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setIngredients(List<String> ingredients){
        this.ingredients=ingredients;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("price", price)
                .add("ingredients", ingredients)
                .add("active", active)
                .toString();
    }

    public Burger toBurger(){
        return new Burger(id.toString(),name,price,ingredients);
    }
}

