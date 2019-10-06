package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@MappedSuperclass
public class CommodityInfo extends AbstractEntity {
    @Version
    @Column(name = "VERSION")
    protected int version;

    @Column(nullable = false)
    protected String name;

    @Column(name="short_description",nullable = false, length = 256)
    protected String shortDescription;

    @Column(nullable = false, length = 2048)
    protected String overview;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_of_creation", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    protected Date dateOfCreation;

    @ManyToOne(optional=false)
    @JoinColumn(name="type_id", referencedColumnName="id")
    protected CommodityType type;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval=true, mappedBy = "commodity", targetEntity=CommodityImage.class, fetch = FetchType.EAGER)
    @org.hibernate.annotations.OrderBy(clause = "image_order asc")
    @org.hibernate.annotations.BatchSize(size=10)
    protected List<CommodityImage> images = new ArrayList<>();

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    @Override public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommodityInfo)) return false;
        if (!super.equals(object)) return false;
        CommodityInfo that = (CommodityInfo) object;
        return getVersion() == that.getVersion() &&
                getName().equals(that.getName()) &&
                getShortDescription().equals(that.getShortDescription()) &&
                getOverview().equals(that.getOverview()) &&
                getDateOfCreation().equals(that.getDateOfCreation()) &&
                getType().equals(that.getType());
    }

    @Override public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getShortDescription(), getOverview(), getType());
    }
}
