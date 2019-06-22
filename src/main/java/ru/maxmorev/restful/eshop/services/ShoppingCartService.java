package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;

public interface ShoppingCartService {

    ShoppingCart createEmptyShoppingCart();
    ShoppingCart findShoppingCartById(Long id);
    ShoppingCart addToShoppingCartSet(ShoppingCartSet set, Integer amount);
    ShoppingCart removeFromShoppingCartSet(ShoppingCartSet set, Integer amount);
    ShoppingCartSet findByBranchAndShoppingCart(CommodityBranch branch, ShoppingCart cart);

}
