package pl.edu.uam.restapi.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.persistence.*;


@Entity
@Table(name = "orders")
@NamedQueries({
        @NamedQuery(name = "orders.findAll", query = "SELECT u FROM OrderEntity u")
})
public class OrderEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(BurgerEntity.class);

    // auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //fields can be renamed
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="burgerId")
    private BurgerEntity burger;

    //fields can be renamed
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId")
    private UserEntity user;

    //fields can be renamed
    @Column(name = "date")
    private String date;

    //fields can be indexed for better performance
    private boolean active = false;

    //Lifecycle methods -- Pre/PostLoad, Pre/PostPersist...
    @PostLoad
    private void postLoad() {
        LOGGER.info("postLoad: {}", toString());
    }

    public OrderEntity() {
    }

    public OrderEntity(BurgerEntity burger,UserEntity user,String date, boolean active) {
        this.burger = burger;
        this.user = user;
        this.date=date;
        this.active=active;
    }

    public OrderEntity(Long id,BurgerEntity burger,UserEntity user,String date) {
        this.id=id;
        this.burger = burger;
        this.user = user;
        this.date=date;
    }

    public OrderEntity(Long id,BurgerEntity burger,UserEntity user,String date,boolean active) {
        this.id=id;
        this.burger = burger;
        this.user = user;
        this.date=date;
        this.active=active;
    }
    public Long getId() {
        return id;
    }

    public BurgerEntity getBurger() {
        return burger;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getDate(){
        return date;
    }

    public boolean isActive() {
        return active;
    }

    public void setBurger(BurgerEntity burger) {
        this.burger = burger;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setDate(String date){
        this.date=date;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("burger", burger)
                .add("user", user)
                .add("date", date)
                .add("active", active)
                .toString();
    }
}

