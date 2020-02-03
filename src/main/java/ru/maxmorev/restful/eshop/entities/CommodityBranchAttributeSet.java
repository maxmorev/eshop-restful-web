package ru.maxmorev.restful.eshop.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "commodity_branch_attribute_set")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityBranchAttributeSet {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR_BRANCH_ATTRIBUTE_SET)
    @Column(updatable = false)
    protected Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_BRANCH_SET_BRANCH"))
    @JsonIgnore
    private CommodityBranch branch;

    @ManyToOne(optional = false)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_BRANCH_SET_ATTRIBUTE"))
    private CommodityAttribute attribute;

    @ManyToOne(optional = false)
    @JoinColumn(name = "attribute_value_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_BRANCH_SET_ATTRIBUTE_VALUE"))
    private CommodityAttributeValue attributeValue;

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
        if (!(object instanceof CommodityBranchAttributeSet)) return false;
        CommodityBranchAttributeSet that = (CommodityBranchAttributeSet) object;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getBranch().getId(), that.getBranch().getId()) &&
                Objects.equals(getAttribute().getId(), that.getAttribute().getId()) &&
                getAttributeValue().equals(that.getAttributeValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBranch(), getAttribute(), getAttributeValue());
    }
}
