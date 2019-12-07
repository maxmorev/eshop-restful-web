package ru.maxmorev.restful.eshop.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import ru.maxmorev.restful.eshop.validation.CheckCommodityBranchAttributes;


import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

/**
 * Proxy POJO for creation commodity
 * this class will be extracted into classes: Commodity, CommodityBranch
 */
@Data
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
    @NotNull
    private String currencyCode;
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

    @JsonIgnore
    @AssertTrue(message = "{validation.commodity.currencyCode.exist}")
    public boolean isValidCurrencyCode(){
        if(Currency.getAvailableCurrencies().stream().anyMatch( c-> c.getCurrencyCode().equals(currencyCode))){
            return true;
        }
        return false;
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
