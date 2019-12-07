package ru.maxmorev.restful.eshop.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

//Readonly
@Embeddable
public class PurchaseId implements Serializable {

    @Column(name = "branch_id")
    Long  branchId;
    @Column(name = "order_id")
    Long orderId;

    protected PurchaseId() {
    }

    public PurchaseId(CommodityBranch branch, CustomerOrder customerOrder){
        if(branch == null) throw new IllegalArgumentException("branch cannot be null");
        if(customerOrder == null) throw new IllegalArgumentException("order cannot be null");
        if(branch.getId() == null) throw new IllegalArgumentException("branch.id cannot be null");
        if(customerOrder.getId() == null) throw new IllegalArgumentException("order.id cannot be null");
        this.branchId = branch.getId();
        this.orderId = customerOrder.getId();
    }

    public Long getBranchId() {
        return branchId;
    }

    public Long getOrderId() {
        return orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseId)) return false;
        PurchaseId that = (PurchaseId) o;
        return getBranchId().equals(that.getBranchId()) &&
                getOrderId().equals(that.getOrderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBranchId(), getOrderId());
    }
}

