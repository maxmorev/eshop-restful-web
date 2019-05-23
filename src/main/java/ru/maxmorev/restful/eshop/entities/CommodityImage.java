package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "commodity_image")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityImage {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "commodity_id", nullable = false)
    private Long commodityId;

    @Column(nullable = false)
    private String uri;

    private Integer width;

    private Integer height;

    /*@JsonIgnore
    @JoinColumn(name="commodity_id", referencedColumnName="id", insertable=false, updatable=false)
    private Commodity commodity;
*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
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

    @Override
    public int hashCode() {
        return Objects.hash(id, uri);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommodityImage that = (CommodityImage) o;
        return id.equals(that.id) &&
                commodityId.equals(that.commodityId) &&
                uri.equals(that.uri) &&
                Objects.equals(width, that.width) &&
                Objects.equals(height, that.height);
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
