package ru.maxmorev.restful.eshop.controllers.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.maxmorev.restful.eshop.annotation.CheckAttributeValueDuplicationForType;
import ru.maxmorev.restful.eshop.annotation.CheckCommodityBranchAttributes;

import javax.validation.Payload;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

/**
 * Proxy POJO for creation commodity
 * this class will be extracted into classes: Commodity, CommodityBranch
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@CheckCommodityBranchAttributes(message = "{validation.commodity.attribute.values}")
public class RequestCommodity {

    @NotBlank(message = "{validation.commodity.name.NotBlank.message}")
    @Size(min=8, max=256, message = "{validation.commodity.name.size.message}")
    private String name;
    @NotBlank(message = "{validation.commodity.shortDescription.NotBlank.message}")
    @Size(min=16, max=256, message = "{validation.commodity.shortDescription.size.message}")
    private String shortDescription;
    @NotBlank(message = "{validation.commodity.overview.NotBlank.message}")
    @Size(min=64, max=2048, message = "{validation.commodity.overview.size.message}")
    private String overview;
    @NotNull(message = "{validation.commodity.amount.NotNull.message}")
    @Min(value = 1, message = "{validation.commodity.amount.min.message}")
    private Integer amount;
    @NotNull(message = "{validation.commodity.price.NotNull.message}")
    private Float price;
    @NotNull(message = "{validation.commodity.typeId.NotNull.message}")
    private Long typeId;
    @NotNull(message = "{validation.commodity.propertyValues.NotNull.message}")
    @Size(min=1, message = "{validation.commodity.propertyValues.size.message}")
    List<Long> propertyValues;
    @NotNull(message = "{validation.commodity.images.NotNull.message}")
    @Size(min=1, message = "{validation.commodity.images.size.message}")
    List<String> images;
    private Long branchId;

    @JsonIgnore
    @AssertTrue(message = "{validation.commodity.price.gt.zero}")
    public boolean isPriceGreaterZero(){
        if(Objects.nonNull(price) && price>0.0f){
            return true;
        }
        return false;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public List<Long> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(List<Long> propertyValues) {
        this.propertyValues = propertyValues;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
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
