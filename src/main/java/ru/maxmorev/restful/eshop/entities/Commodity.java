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
public class Commodity extends AbstractEntity {

        @Version
        @Column(name = "VERSION")
        private int version;

        @Column(nullable = false)
        private String name;

        @Column(name="short_description",nullable = false, length = 256)
        private String shortDescription;

        @Column(nullable = false, length = 2048)
        private String overview;

        @Temporal(TemporalType.DATE)
        @Column(name="date_of_creation", nullable = false)
        private Date dateOfCreation;


        @ManyToOne(optional=false)
        @JoinColumn(name="type_id", referencedColumnName="id")
        private CommodityType type;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "commodityId", targetEntity=CommodityImage.class, fetch = FetchType.EAGER)
        private List<CommodityImage> images = new ArrayList<>();

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "commodity", targetEntity=CommodityBranch.class, fetch = FetchType.LAZY)
        private Set<CommodityBranch> branches = new HashSet<>();

        public Commodity(){
                super();
                this.dateOfCreation = new Date();
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getShortDescription() {
                return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
                this.shortDescription = shortDescription;
        }

        public String getOverview() {
                return overview;
        }

        public void setOverview(String overview) {
                this.overview = overview;
        }

        public Date getDateOfCreation() {
                return dateOfCreation;
        }

        public void setDateOfCreation(Date dateOfCreation) {
                this.dateOfCreation = dateOfCreation;
        }

        public CommodityType getType() {
                return type;
        }

        public void setType(CommodityType type) {
                this.type = type;
        }

        public List<CommodityImage> getImages() {
                return images;
        }

        public void setImages(List<CommodityImage> images) {
                this.images = images;
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
                Commodity that = (Commodity) o;
                if (!name.equals(that.name))
                        return false;
                return true;
        }

        @Override public int hashCode() {
                int result = super.hashCode();
                result = 31 * result + (name != null ? name.hashCode() : 0);
                result = 31 * result + (shortDescription != null ? shortDescription.hashCode() : 0);
                result = 31 * result + (overview != null ? overview.hashCode() : 0);
                result = 31 * result + (dateOfCreation != null ? dateOfCreation.hashCode() : 0);
                return result;
        }
}