package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Short getImageOrder() {
        return imageOrder;
    }

    public void setImageOrder(Short imageOrder) {
        this.imageOrder = imageOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, commodity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommodityImage that = (CommodityImage) o;
        if(!uri.equals(that.uri)){
           return false;
        }
        return commodity.equals(that.commodity);
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
