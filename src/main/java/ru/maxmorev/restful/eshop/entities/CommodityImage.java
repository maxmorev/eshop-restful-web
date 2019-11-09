package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@Data
@Entity
@Table(name = "commodity_image")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityImage {
    @Id
    @Column(name="uri", nullable = false, unique = true)
    private String uri;

    @ManyToOne(optional=false)
    @JoinColumn(name="commodity_id", referencedColumnName="id")
    @JsonIgnore
    private Commodity commodity;

    private Integer width;
    private Integer height;

    @Column(nullable = false, name="image_order")
    private Short imageOrder;

    @Override
    public int hashCode() {
        return Objects.hash(uri, commodity);
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommodityImage)) return false;
        if (!super.equals(object)) return false;
        CommodityImage that = (CommodityImage) object;
        return getUri().equals(that.getUri());
    }
}
