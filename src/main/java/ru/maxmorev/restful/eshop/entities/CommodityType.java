package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "commodity_type")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityType extends AbstractEntity {

    @Version
    @Column(name = "VERSION")
    private int version;

    @Column(unique = true, updatable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "commodityType", targetEntity= CommodityAttribute.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CommodityAttribute> attributes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "type", targetEntity=Commodity.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Commodity> commodities = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CommodityAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<CommodityAttribute> attributes) {
        this.attributes = attributes;
    }

    public Set<Commodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(Set<Commodity> commodities) {
        this.commodities = commodities;
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

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        CommodityType that = (CommodityType) o;
        if (!name.equals(that.name))
            return false;
        if (!description.equals(that.description))
            return false;
        return true;
    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }


}

