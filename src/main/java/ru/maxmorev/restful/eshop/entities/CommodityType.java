package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "commodity_type")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityType extends AbstractEntity {

    @Version
    @Column(name = "VERSION")
    private int version;

    @NotBlank(message = "{validation.CommodityType.name.NotBlank.message}")
    @Column(unique = true, updatable = false)
    private String name;

    @NotBlank(message = "{validation.CommodityType.description.NotBlank.message}")
    @Size(min = 8, max = 128, message = "{validation.CommodityType.description.size.message}")
    @Column(nullable = false, length = 128)
    private String description;

    @org.hibernate.annotations.BatchSize(size = 5)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "commodityType", targetEntity = CommodityAttribute.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CommodityAttribute> attributes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "type", targetEntity = Commodity.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Commodity> commodities = new HashSet<>();

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
        if (!(object instanceof CommodityType)) return false;
        if (!super.equals(object)) return false;
        CommodityType that = (CommodityType) object;
        return version == that.version &&
                getName().equals(that.getName()) &&
                getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), version, getName(), getDescription());
    }
}

