package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
@Data
@Entity
@Table(name = "shopping_cart_set")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCartSet extends AbstractEntity {

    @Column(name="amount", nullable = false)
    private Integer amount = 1;

    @ManyToOne(optional=false)
    @JoinColumn(name="branch_id", referencedColumnName="id")
    private CommodityBranch branch;

    @JsonIgnore
    @ManyToOne(optional=false)
    @JoinColumn(name="shopping_cart_id", referencedColumnName="id")
    private ShoppingCart shoppingCart;

    public CommodityInfo getCommodityInfo(){
        return branch.getCommodity();
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
        if (!(object instanceof ShoppingCartSet)) return false;
        if (!super.equals(object)) return false;
        ShoppingCartSet that = (ShoppingCartSet) object;
        return getAmount().equals(that.getAmount()) &&
                getBranch().equals(that.getBranch()) &&
                getShoppingCart().equals(that.getShoppingCart());
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), getAmount(), getBranch(), getShoppingCart());
    }
}
