package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "shopping_cart")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCart extends AbstractEntity {

    @Version
    @Column(name = "VERSION")
    private int version;

    @org.hibernate.annotations.BatchSize(size = 5)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "shoppingCart", targetEntity = ShoppingCartSet.class, fetch = FetchType.LAZY)
    private Set<ShoppingCartSet> shoppingSet;

    public int getItemsAmount() {
        return shoppingSet != null ? shoppingSet.stream().mapToInt(ShoppingCartSet::getAmount).sum() : 0;
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
        if (!(object instanceof ShoppingCart)) return false;
        if (!super.equals(object)) return false;
        ShoppingCart that = (ShoppingCart) object;
        return version == that.version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), version);
    }
}
