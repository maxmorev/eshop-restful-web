package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "purchase")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Purchase {

    @EmbeddedId
    private PurchaseId id;

    @Column(name="amount", nullable = false)
    private Integer amount = 1;

    @ManyToOne(optional=false, cascade = CascadeType.PERSIST)
    @JoinColumn(name="branch_id", referencedColumnName="id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_PURCHASE_BRANCH"))
    private CommodityBranch branch;

    @ManyToOne(optional=false, cascade = CascadeType.PERSIST)
    @JoinColumn(name="order_id", referencedColumnName="id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_PURCHASE_CUSTOMER_ORDER"))
    private CustomerOrder customerOrder;

    public Purchase(CommodityBranch branch, CustomerOrder customerOrder, Integer amount){
        this.id = new PurchaseId(branch, customerOrder);
        this.amount = amount;
        this.branch = branch;
        this.customerOrder = customerOrder;
    }

    protected Purchase() {
    }

    public PurchaseId getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public CommodityBranch getBranch() {
        return branch;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase)) return false;
        Purchase purchase = (Purchase) o;
        return getId().equals(purchase.getId()) &&
                getAmount().equals(purchase.getAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAmount());
    }
}
