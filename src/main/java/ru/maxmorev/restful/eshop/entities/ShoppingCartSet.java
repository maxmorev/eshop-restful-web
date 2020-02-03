package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "shopping_cart_set")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCartSet {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR_SHOPPING_CART_SET)
    @Column(updatable = false)
    protected Long id;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private CommodityBranch branch;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_SHOPPING_CART_SET_CART"))
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
        ShoppingCartSet that = (ShoppingCartSet) object;
        return Objects.equals(getId(), that.getId())
                && getAmount().equals(that.getAmount()) &&
                getBranch().equals(that.getBranch()) &&
                getShoppingCart().equals(that.getShoppingCart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAmount(), getBranch(), getShoppingCart());
    }
}
