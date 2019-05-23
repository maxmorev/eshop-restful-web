package ru.maxmorev.restful.eshop.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;

@Entity(name = "commodity_branch_attribute_set")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityBranchAttributeSet {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(name = "attribute_id", nullable = false)
    private Long attributeId;

    @Column(name = "attribute_value_id", nullable = false)
    private Long attributeValueId;

    @ManyToOne(optional=false)
    @JoinColumn(name="attribute_id", referencedColumnName="id", insertable=false, updatable=false)
    private CommodityAttribute attribute;

    @ManyToOne(optional=false)
    @JoinColumn(name="attribute_value_id", referencedColumnName="id", insertable=false, updatable=false)
    private CommodityAttributeValue attributeValue;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getAttributeValueId() {
        return attributeValueId;
    }

    public void setAttributeValueId(Long attributeValueId) {
        this.attributeValueId = attributeValueId;
    }

    public CommodityAttributeValue getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(CommodityAttributeValue attributeValue) {
        this.attributeValue = attributeValue;
    }

    public CommodityAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(CommodityAttribute attribute) {
        this.attribute = attribute;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
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
