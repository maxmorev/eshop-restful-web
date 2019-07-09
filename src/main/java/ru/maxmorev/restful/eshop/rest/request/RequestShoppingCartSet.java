package ru.maxmorev.restful.eshop.rest.request;

import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;

/**
 * The POJO class to build from the JSON request with id's for persistence level class ShoppingCartSet
 * @see ShoppingCartSet
 */
public class RequestShoppingCartSet {
    //TODO Validation
    private Long shoppingCartId;
    private Long branchId;
    private Integer amount;

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
