package ru.maxmorev.restful.eshop.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.maxmorev.restful.eshop.annotation.AttributeDataType;

import javax.persistence.*;
import java.util.List;

@Entity(name = "commodity_attribute")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityAttribute {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(updatable = false)
    private String name;

    @Column(name = "data_type", nullable = false)
    AttributeDataType dataType;

    @Column(name = "attribute_measure")
    private String measure;

    @Column(name = "type_id", nullable = false)
    private Long typeId;

    @ManyToOne(optional=false)
    @JoinColumn(name="type_id", referencedColumnName="id", insertable=false, updatable=false)
    @JsonIgnore
    private CommodityType commodityType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attributeId", targetEntity= CommodityAttributeValue.class, fetch = FetchType.EAGER)
    //@JsonIgnore
    private List<CommodityAttributeValue> values;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public List<CommodityAttributeValue> getValues() {
        return values;
    }

    public void setValues(List<CommodityAttributeValue> values) {
        this.values = values;
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
