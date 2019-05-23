package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "commodity_attribute_value")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityAttributeValue {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(length = 256)
    private String string;
    @Column(length = 2048)
    private String text;
    @Column
    private Float real;
    @Column
    private Long integer;

    @Column(name = "attribute_id", nullable = false)
    private Long attributeId;

    @ManyToOne(optional=false)
    @JoinColumn(name="attribute_id", referencedColumnName="id", insertable=false, updatable=false)
    @JsonIgnore
    private CommodityAttribute attribute;

    public CommodityAttributeValue(){
        super();
    }

    public CommodityAttributeValue(CommodityAttribute attribute){
        this.attribute = attribute;
    }

    public void setValue(Object v){
        if(Objects.nonNull(attribute)){
            switch (attribute.dataType) {
                case String: this.string = (String)v;break;
                case Text: this.text = (String)v;break;
                case Float: this.real = (Float)v;break;
                case Integer: this.integer = (Long)v; break;
            }
        }
    }

    public Object getValue(){
        if(Objects.nonNull(attribute)){
            switch (attribute.dataType) {
                case String: return this.string;
                case Text: return this.text;
                case Float: return this.real;
                case Integer: return this.integer;
            }
        }
        return null;
    }

    @JsonIgnore
    public  Object createValueFrom(String val){
        if(Objects.nonNull(attribute)){
            switch (attribute.dataType) {
                case String: return val;
                case Text: return val;
                case Float: return Float.valueOf(val);
                case Integer: return Long.valueOf(val);
            }
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getString() {
        return string;
    }

    public void setString(String value) {
        this.string = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getReal() {
        return real;
    }

    public void setReal(Float real) {
        this.real = real;
    }

    public Long getInteger() {
        return integer;
    }

    public void setInteger(Long integer) {
        this.integer = integer;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public CommodityAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(CommodityAttribute attribute) {
        this.attribute = attribute;
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
