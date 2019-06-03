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

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        CommodityBranchAttributeSet that = (CommodityBranchAttributeSet) o;
        if (!branch.equals(that.branch))
            return false;
        if(!Objects.equals(attribute, that.attribute))
            return false;
        return Objects.equals(attributeValue, that.attributeValue);

    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (branch != null ? branch.hashCode() : 0);
        result = 31 * result + (attribute != null ? attribute.hashCode() : 0);
        result = 31 * result + (attributeValue != null ? attributeValue.hashCode() : 0);
        return result;
    }



}
