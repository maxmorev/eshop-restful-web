package ru.maxmorev.restful.eshop.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "commodity")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commodity {

        @Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        private Long id;

        @Column(name = "type_id", nullable = false)
        private Long typeId;

        @Column(unique = true, updatable = false)
        private String code;//Unique code use codeGenerator

        @Column(nullable = false)
        private String name;

        @Column(name="short_description",nullable = false, length = 256)
        private String shortDescription;

        @Column(nullable = false, length = 2048)
        private String overview;

        @Column(name="date_of_creation", nullable = false)
        private Date dateOfCreation;


        @ManyToOne(optional=false)
        @JoinColumn(name="type_id", referencedColumnName="id", insertable=false, updatable=false)
        private CommodityType type;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "commodityId", targetEntity=CommodityImage.class, fetch = FetchType.EAGER)
        //@JsonIgnore
        private List<CommodityImage> images;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "commodityId", targetEntity=CommodityBranch.class, fetch = FetchType.LAZY)
        //@JsonIgnore
        private List<CommodityBranch> branches;

        public Commodity(){
                super();
                this.dateOfCreation = new Date();
                this.code = String.valueOf( dateOfCreation.getTime() );
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Long getTypeId() {
                return typeId;
        }

        public void setTypeId(Long typeId) {
                this.typeId = typeId;
        }

        public String getCode() {
                return code;
        }

        public void setCode(String code) {
                this.code = code;
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

        public List<CommodityBranch> getBranches() {
                return branches;
        }

        public void setBranches(List<CommodityBranch> branches) {
                this.branches = branches;
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