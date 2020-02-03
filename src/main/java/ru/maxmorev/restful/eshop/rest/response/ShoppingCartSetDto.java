package ru.maxmorev.restful.eshop.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCartSetDto {
    private Integer amount;
    private CommodityBranchDto branch;
    private CommodityInfoDto commodityInfo;

    public static ShoppingCartSetDto of(ShoppingCartSet scs){
        return ShoppingCartSetDto.builder()
                .amount(scs.getAmount())
                .branch(CommodityBranchDto.of(scs.getBranch()))
                .commodityInfo(CommodityInfoDto.of(scs.getCommodityInfo()))
                .build();
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
