package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.List;

@Entity(name = "shopping_cart")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name="consumer_id")
    private Long consumerId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shoppingCartId", targetEntity=ShoppingCartSet.class, fetch = FetchType.EAGER)
    //@JsonIgnore
    private List<ShoppingCartSet> shoppingSet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public List<ShoppingCartSet> getShoppingSet() {
        return shoppingSet;
    }

    public void setShoppingSet(List<ShoppingCartSet> shoppingSet) {
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
