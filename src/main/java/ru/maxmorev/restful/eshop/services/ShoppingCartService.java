package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;

public interface ShoppingCartService {

    ShoppingCart createEmptyShoppingCart();
    ShoppingCart findShoppingCartById(Long id);
    ShoppingCart removeFromShoppingCartSet(ShoppingCartSet set, Integer amount);
    ShoppingCartSet findByBranchAndShoppingCart(CommodityBranch branch, ShoppingCart cart);
    ShoppingCart update(ShoppingCart sc);
    ShoppingCart addBranchToShoppingCart(Long branchId, Integer amount, Long shoppingCartId);
    ShoppingCart mergeFromTo(ShoppingCart from, ShoppingCart to);
    ShoppingCart checkAvailability(ShoppingCart sc);

}
