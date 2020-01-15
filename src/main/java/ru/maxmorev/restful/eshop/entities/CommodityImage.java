package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "commodity_image")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityImage {
    @Id
    @Column(name = "uri", nullable = false, unique = true)
    private String uri;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commodity_id", referencedColumnName = "id")
    @JsonIgnore
    private Commodity commodity;

    private Integer width;
    private Integer height;

    @Column(nullable = false, name = "image_order")
    private Short imageOrder;

    @Override
    public int hashCode() {
        return Objects.hash(uri, commodity);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommodityImage)) return false;
        if (!super.equals(object)) return false;
        CommodityImage that = (CommodityImage) object;
        return getUri().equals(that.getUri());
    }
}
