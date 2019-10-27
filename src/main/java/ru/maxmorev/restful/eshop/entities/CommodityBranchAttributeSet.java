package ru.maxmorev.restful.eshop.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@Data
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
        return Objects.equals( getBranch().getId(), that.getBranch().getId() ) &&
                Objects.equals( getAttribute().getId(), that.getAttribute().getId()) &&
                getAttributeValue().equals(that.getAttributeValue());
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), getBranch(), getAttribute(), getAttributeValue());
    }
}
