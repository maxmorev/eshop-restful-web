package ru.maxmorev.restful.eshop.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.CommodityType;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;

import java.util.Optional;

@Repository
public interface ShoppingCartSetRepository extends CrudRepository<ShoppingCartSet, Long> {

    Optional<ShoppingCartSet> findByBranchAndShoppingCart(CommodityBranch branch, ShoppingCart shoppingCart);
}
