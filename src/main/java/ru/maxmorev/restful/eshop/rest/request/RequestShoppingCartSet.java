package ru.maxmorev.restful.eshop.rest.request;

import lombok.Data;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;

/**
 * The POJO class to build from the JSON request with id's for persistence level class ShoppingCartSet
 * @see ShoppingCartSet
 */
@Data
public class RequestShoppingCartSet {
    //TODO Validation
    private Long shoppingCartId;
    private Long branchId;
    private Integer amount;
}
