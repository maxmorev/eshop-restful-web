package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;

@Entity
@Table(name = "shopping_cart_set")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCartSet extends AbstractEntity {

    @Column(name="amount", nullable = false)
    private Integer amount = 1;

    @ManyToOne(optional=false)
    @JoinColumn(name="branch_id", referencedColumnName="id")
    private CommodityBranch branch;

    @ManyToOne(optional=false)
    @JoinColumn(name="shopping_cart_id", referencedColumnName="id")
    @JsonIgnore
    private ShoppingCart shoppingCart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public CommodityBranch getBranch() {
        return branch;
    }

    public void setBranch(CommodityBranch branch) {
        this.branch = branch;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
