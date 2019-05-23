package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.List;

@Entity(name = "commodity_type")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityType {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(unique = true, updatable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    //cascade = CascadeType.ALL
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeId", targetEntity= CommodityAttribute.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CommodityAttribute> properties;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeId", targetEntity=Commodity.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Commodity> commodities;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CommodityAttribute> getProperties() {
        return properties;
    }

    public void setProperties(List<CommodityAttribute> properties) {
        this.properties = properties;
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

