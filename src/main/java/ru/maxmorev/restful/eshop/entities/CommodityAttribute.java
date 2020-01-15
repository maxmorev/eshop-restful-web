package ru.maxmorev.restful.eshop.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import ru.maxmorev.restful.eshop.annotation.AttributeDataType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "commodity_attribute")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityAttribute extends AbstractEntity {

    @Column(updatable = false, length = 64)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 8)
    AttributeDataType dataType;

    @Column(name = "attribute_measure")
    private String measure;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @JsonIgnore
    private CommodityType commodityType;

    @org.hibernate.annotations.BatchSize(size = 5)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "attribute", targetEntity = CommodityAttributeValue.class, fetch = FetchType.LAZY)
    private Set<CommodityAttributeValue> values = new HashSet<>();

    @org.hibernate.annotations.BatchSize(size = 5)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute", orphanRemoval = true, targetEntity = CommodityBranchAttributeSet.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CommodityBranchAttributeSet> attributeSet = new HashSet<>();

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
        if (!(object instanceof CommodityAttribute)) return false;
        if (!super.equals(object)) return false;
        CommodityAttribute that = (CommodityAttribute) object;
        return getName().equals(that.getName()) &&
                getDataType().equals(that.getDataType()) &&
                Objects.equals(getMeasure(), that.getMeasure()) &&
                Objects.equals(getCommodityType().getId(), that.getCommodityType().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getDataType(), getCommodityType());
    }
}
