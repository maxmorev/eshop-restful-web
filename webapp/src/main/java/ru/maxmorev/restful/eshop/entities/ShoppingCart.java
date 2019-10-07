package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "shopping_cart")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCart extends AbstractEntity{

    @Version
    @Column(name = "VERSION")
    private int version;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="customer_id", referencedColumnName = "id")
    private Customer customer;

    @org.hibernate.annotations.BatchSize(size=5)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "shoppingCart", targetEntity=ShoppingCartSet.class, fetch = FetchType.LAZY)
    private Set<ShoppingCartSet> shoppingSet;

    public Set<ShoppingCartSet> getShoppingSet() {
        return shoppingSet;
    }

    public void setShoppingSet(Set<ShoppingCartSet> shoppingSet) {
        this.shoppingSet = shoppingSet;
    }

    @JsonIgnore
    public Customer getCustomer() {
        return customer;
    }

    public int getItemsAmount(){
        return shoppingSet!=null? shoppingSet.stream().collect(Collectors.summingInt(s->s.getAmount())) : 0;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ShoppingCart)) return false;
        if (!super.equals(object)) return false;
        ShoppingCart that = (ShoppingCart) object;
        return version == that.version &&
                java.util.Objects.equals(getCustomer(), that.getCustomer());
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), version, getCustomer());
    }
}
