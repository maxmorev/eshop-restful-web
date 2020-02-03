package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@MappedSuperclass
public class CommodityInfo {
    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR_COMMODITY)
    @Column(updatable = false)
    protected Long id;

    @Version
    @Column(name = "VERSION")
    protected int version;

    @Column(nullable = false)
    protected String name;

    @Column(name = "short_description", nullable = false, length = 256)
    protected String shortDescription;

    @Column(nullable = false, length = 2048)
    protected String overview;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_of_creation", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    protected Date dateOfCreation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_COMMODITY_TYPE"))
    protected CommodityType type;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "commodity", targetEntity = CommodityImage.class, fetch = FetchType.EAGER)
    @org.hibernate.annotations.OrderBy(clause = "image_order asc")
    @org.hibernate.annotations.BatchSize(size = 10)
    protected List<CommodityImage> images = new ArrayList<>();

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommodityInfo)) return false;
        CommodityInfo that = (CommodityInfo) object;
        return Objects.equals(getId(), that.getId())
                && getVersion() == that.getVersion() &&
                getName().equals(that.getName()) &&
                getShortDescription().equals(that.getShortDescription()) &&
                getOverview().equals(that.getOverview()) &&
                getDateOfCreation().equals(that.getDateOfCreation()) &&
                getType().equals(that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getShortDescription(), getOverview(), getType());
    }
}
