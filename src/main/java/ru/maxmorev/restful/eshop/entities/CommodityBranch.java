package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "commodity_branch")
@JsonIgnoreProperties(ignoreUnknown = true)
@org.hibernate.annotations.BatchSize(size=10)
public class CommodityBranch extends AbstractEntity{

    @Version
    @Column(name = "VERSION")
    private int version;

    @ManyToOne(optional=false)
    @JoinColumn(name="commodity_id", referencedColumnName="id")
    @JsonIgnore
    private Commodity commodity;

    private Integer amount; //amount of items in branch

    private Float price; //price for 1 item in branch

    @Column(nullable = false)
    private Currency currency; //current price currency

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branch", orphanRemoval=true, targetEntity= CommodityBranchAttributeSet.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.BatchSize(size=10)
    private Set<CommodityBranchAttributeSet> attributeSet = new HashSet<>();

    public Long getCommodityId(){
        return this.commodity.getId();
    }

    public String getCode(){
        return commodity.getId()+"-" + this.getId();
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Set<CommodityBranchAttributeSet> getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(Set<CommodityBranchAttributeSet> attributeSet) {
        this.attributeSet = attributeSet;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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
        if (!(object instanceof CommodityBranch)) return false;
        if (!super.equals(object)) return false;
        CommodityBranch that = (CommodityBranch) object;
        return version == that.version &&
                getCommodity().equals(that.getCommodity()) &&
                getAmount().equals(that.getAmount()) &&
                getPrice().equals(that.getPrice()) &&
                getCurrency().equals(that.getCurrency());
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), version, getCommodity(), getAmount(), getPrice(), getCurrency());
    }
}
