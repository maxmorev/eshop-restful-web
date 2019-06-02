package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "commodity_attribute_value")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityAttributeValue extends AbstractEntity{

    @Column(length = 256)
    private String string;

    @Column(length = 2048)
    private String text;

    @Column
    private Float real;

    @Column
    private Long integer;

    @ManyToOne(optional=false)
    @JoinColumn(name="attribute_id", referencedColumnName="id")
    @JsonIgnore
    private CommodityAttribute attribute;

    public CommodityAttributeValue(){
        super();
    }

    public CommodityAttributeValue(CommodityAttribute attribute){
        super();
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

    public CommodityAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(CommodityAttribute attribute) {
        this.attribute = attribute;
    }

    @Override public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        CommodityAttributeValue that = (CommodityAttributeValue) o;
        if (!Objects.equals(string, that.string))
            return false;
        if (!Objects.equals(text, that.text))
            return false;
        if (!Objects.equals(real, that.real))
            return false;
        return Objects.equals(integer, that.integer);
    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (string != null ? string.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (real != null ? real.hashCode() : 0);
        result = 31 * result + (integer != null ? integer.hashCode() : 0);
        return result;
    }
}
