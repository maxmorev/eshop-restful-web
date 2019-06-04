package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Temporal(TemporalType.DATE)
    @Column(name="date_of_creation", nullable = false)
    protected Date dateOfCreation;

    @ManyToOne(optional=false)
    @JoinColumn(name="type_id", referencedColumnName="id")
    protected CommodityType type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "commodity", targetEntity=CommodityImage.class, fetch = FetchType.EAGER)
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
