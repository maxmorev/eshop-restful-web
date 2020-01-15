package ru.maxmorev.restful.eshop.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "commodity")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commodity extends CommodityInfo {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "commodity", targetEntity = CommodityBranch.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.BatchSize(size = 10)
    private List<CommodityBranch> branches = new ArrayList<>();

    public Float getPrice() {
        return branches.isEmpty() ? 0.0f : branches.get(0).getPrice();
    }

    public String getCodeIfSingle() {
        return branches.size() == 1 ? branches.get(0).getCode() : "";
    }

    @JsonIgnore
    public String getCurrencyCode() {
        return branches.isEmpty() ? "" : branches.get(0).getCurrency().getCurrencyCode();
    }

    @JsonIgnore
    public String getLastImageUri() {
        int size = images.size();
        if (size > 0) {
            return images.get(size - 1).getUri();
        } else {
            return "";
        }
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Commodity)) return false;
        if (!super.equals(o))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        return result;
    }


}