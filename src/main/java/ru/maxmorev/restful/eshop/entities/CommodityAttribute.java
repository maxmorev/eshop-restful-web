package ru.maxmorev.restful.eshop.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.maxmorev.restful.eshop.annotation.AttributeDataType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "commodity_attribute")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityAttribute extends AbstractEntity{

    @Column(updatable = false, length = 64)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 8)
    AttributeDataType dataType;

    @Column(name = "attribute_measure")
    private String measure;

    @ManyToOne(optional=false)
    @JoinColumn(name="type_id", referencedColumnName="id")
    @JsonIgnore
    private CommodityType commodityType;

    @org.hibernate.annotations.BatchSize(size=5)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "attribute", targetEntity= CommodityAttributeValue.class, fetch = FetchType.LAZY)
    private Set<CommodityAttributeValue> values = new HashSet<>();

    @org.hibernate.annotations.BatchSize(size=5)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute", orphanRemoval=true, targetEntity= CommodityBranchAttributeSet.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CommodityBranchAttributeSet> attributeSet = new HashSet<>();

    public Long getTypeId(){ return commodityType.id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeDataType getDataType() {
        return dataType;
    }

    public void setDataType(AttributeDataType dataType) {
        this.dataType = dataType;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public Set<CommodityAttributeValue> getValues() {
        return values;
    }

    public void setValues(Set<CommodityAttributeValue> values) {
        this.values = values;
    }

    public Set<CommodityBranchAttributeSet> getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(Set<CommodityBranchAttributeSet> attributeSet) {
        this.attributeSet = attributeSet;
    }

    @Override public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommodityAttribute)) return false;
        if (!super.equals(object)) return false;
        CommodityAttribute that = (CommodityAttribute) object;
        return getName().equals(that.getName()) &&
                getDataType().equals(that.getDataType()) &&
                java.util.Objects.equals(getMeasure(), that.getMeasure()) &&
                getCommodityType().equals(that.getCommodityType());
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getDataType(), getCommodityType());
    }
}
