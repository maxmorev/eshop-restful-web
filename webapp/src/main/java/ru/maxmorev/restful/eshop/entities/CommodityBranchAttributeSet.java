package ru.maxmorev.restful.eshop.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "commodity_branch_attribute_set")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityBranchAttributeSet extends AbstractEntity {

    @ManyToOne(optional=false)
    @JoinColumn(name="branch_id", referencedColumnName="id")
    @JsonIgnore
    private CommodityBranch branch;

    @ManyToOne(optional=false)
    @JoinColumn(name="attribute_id", referencedColumnName="id")
    private CommodityAttribute attribute;

    @ManyToOne(optional=false)
    @JoinColumn(name="attribute_value_id", referencedColumnName="id")
    private CommodityAttributeValue attributeValue;

    public CommodityBranch getBranch() {
        return branch;
    }

    public void setBranch(CommodityBranch branch) {
        this.branch = branch;
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
        if (!(object instanceof CommodityBranchAttributeSet)) return false;
        if (!super.equals(object)) return false;
        CommodityBranchAttributeSet that = (CommodityBranchAttributeSet) object;
        return getBranch().equals(that.getBranch()) &&
                getAttribute().equals(that.getAttribute()) &&
                getAttributeValue().equals(that.getAttributeValue());
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), getBranch(), getAttribute(), getAttributeValue());
    }
}