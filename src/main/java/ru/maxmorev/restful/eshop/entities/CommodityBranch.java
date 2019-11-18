package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "commodity_branch")
@JsonIgnoreProperties(ignoreUnknown = true)
@org.hibernate.annotations.BatchSize(size = 10)
public class CommodityBranch extends AbstractEntity {

    @Version
    @Column(name = "VERSION")
    private int version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commodity_id", referencedColumnName = "id")
    @JsonIgnore
    private Commodity commodity;

    @Column(nullable = false)
    private Integer amount; //amount of items in branch
    @Column(nullable = false)
    private Float price; //price for 1 item in branch

    @Column(nullable = false)
    private Currency currency; //current price currency

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branch", orphanRemoval = true, targetEntity = CommodityBranchAttributeSet.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.BatchSize(size = 10)
    private Set<CommodityBranchAttributeSet> attributeSet = new HashSet<>();

    public String getCode() {
        return commodity.getId() + "-" + this.getId();
    }

    public Long getCommodityId() {
        return this.commodity.getId();
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommodityBranch)) return false;
        if (!super.equals(object)) return false;
        CommodityBranch that = (CommodityBranch) object;
        return version == that.version &&
                getCommodity().equals(that.getCommodity()) &&
                getAmount().equals(that.getAmount()) &&
                getPrice().equals(that.getPrice()) &&
                getCurrency().equals(that.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), version, getCommodity(), getAmount(), getPrice(), getCurrency());
    }
}
