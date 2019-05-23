package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.List;

@Entity(name = "commodity_branch")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityBranch {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "commodity_id", nullable = false)
    private Long commodityId;

    @ManyToOne(optional=false)
    @JoinColumn(name="commodity_id", referencedColumnName="id", insertable=false, updatable=false)
    @JsonIgnore
    private Commodity commodity;

    private Integer amount; //amount of items in branch

    private Float price; //price for 1 item in branch

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branchId", targetEntity= CommodityBranchAttributeSet.class, fetch = FetchType.LAZY)
    //@JsonIgnore
    private List<CommodityBranchAttributeSet> propertySet;

    public String getCode(){
        return commodity.getId()+"-" + this.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
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

    public List<CommodityBranchAttributeSet> getPropertySet() {
        return propertySet;
    }

    public void setPropertySet(List<CommodityBranchAttributeSet> propertySet) {
        this.propertySet = propertySet;
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
