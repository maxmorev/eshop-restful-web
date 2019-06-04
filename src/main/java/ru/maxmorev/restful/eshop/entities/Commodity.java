package ru.maxmorev.restful.eshop.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "commodity")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commodity extends CommodityInfo {

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "commodity", targetEntity=CommodityBranch.class, fetch = FetchType.LAZY)
        private Set<CommodityBranch> branches = new HashSet<>();

        public Commodity(){
                super();
                this.dateOfCreation = new Date();
        }

        public Set<CommodityBranch> getBranches() {
                return branches;
        }

        public void setBranches(Set<CommodityBranch> branches) {
                this.branches = branches;
        }

        public Float getPrice(){
                return branches.stream().findFirst().get().getPrice();
        }

        @JsonIgnore
        public String getLastImageUri(){
                int size = images.size();
                if(size>0) {
                        return images.get(size - 1).getUri();
                }else{
                        return "";
                }
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
                return true;
        }

        @Override public int hashCode() {
                int result = super.hashCode();
                return result;
        }


}