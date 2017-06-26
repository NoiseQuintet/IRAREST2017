package pl.edu.uam.restapi.database;

import pl.edu.uam.restapi.model.Burger;

import java.util.Collection;

public interface BurgerDatabase {
    Burger getBurger(String id);

    Burger createBurger(Burger user);

    Collection<Burger> getBurgers();

    Burger updateBurger(String userId, Burger user);

    void deleteBurger(String id);
}
