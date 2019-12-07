package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;

import java.util.Optional;

public interface ShoppingCartService {

    ShoppingCart createEmptyShoppingCart();
    Optional<ShoppingCart> findShoppingCartById(Long id);
    ShoppingCart removeBranchFromShoppingCart(Long branchId, Long shoppingCartId, Integer amount);
    ShoppingCart update(ShoppingCart sc);
    ShoppingCart addBranchToShoppingCart(Long branchId, Long shoppingCartId, Integer amount);
    //ShoppingCart mergeFromTo(ShoppingCart from, ShoppingCart to);
    ShoppingCart checkAvailabilityByBranches(ShoppingCart sc);
    ShoppingCart mergeCartFromCookieWithCustomer(ShoppingCart sc, Customer customer);



}
