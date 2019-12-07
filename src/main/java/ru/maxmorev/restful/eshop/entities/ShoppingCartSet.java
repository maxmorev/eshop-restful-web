package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "shopping_cart_set")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCartSet extends AbstractEntity {

    @Column(name = "amount", nullable = false)
    private Integer amount = 1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private CommodityBranch branch;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "id")
    private ShoppingCart shoppingCart;

    public CommodityInfo getCommodityInfo() {
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ShoppingCartSet)) return false;
        if (!super.equals(object)) return false;
        ShoppingCartSet that = (ShoppingCartSet) object;
        return getAmount().equals(that.getAmount()) &&
                getBranch().equals(that.getBranch()) &&
                getShoppingCart().equals(that.getShoppingCart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAmount(), getBranch(), getShoppingCart());
    }
}
