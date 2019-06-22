package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shopping_cart")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCart extends AbstractEntity{

    @Version
    @Column(name = "VERSION")
    private int version;

    @Column(name="consumer_id")
    private Long consumerId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "shoppingCart", targetEntity=ShoppingCartSet.class, fetch = FetchType.LAZY)
    private Set<ShoppingCartSet> shoppingSet;

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public Set<ShoppingCartSet> getShoppingSet() {
        return shoppingSet;
    }

    public void setShoppingSet(Set<ShoppingCartSet> shoppingSet) {
        this.shoppingSet = shoppingSet;
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
