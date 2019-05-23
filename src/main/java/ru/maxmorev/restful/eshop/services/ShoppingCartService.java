package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;

public interface ShoppingCartService {

    ShoppingCart createEmptyShoppingCart();
    ShoppingCart findShoppingCartById(Long id);
    boolean addToShoppingCartSet(ShoppingCartSet set);

}
