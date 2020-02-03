package ru.maxmorev.restful.eshop.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Value;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;

import java.util.Set;
import java.util.stream.Collectors;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCartDto {
    private Long id;
    private Set<ShoppingCartSetDto> shoppingSet;

    public int getItemsAmount() {
        return shoppingSet != null ? shoppingSet.stream().mapToInt(ShoppingCartSetDto::getAmount).sum() : 0;
    }

    public double getTotalPrice() {
        return shoppingSet != null ? shoppingSet.stream().mapToDouble(scs->scs.getAmount()*scs.getBranch().getPrice()).sum(): 0.0d;
    }

    private String getCurrencyCode() {
        return shoppingSet != null ? shoppingSet.stream().findFirst().map(scs->scs.getBranch().getCurrency()).orElse(""):"";
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

    public static ShoppingCartDto of(ShoppingCart sc){
        return ShoppingCartDto.builder()
                .id(sc.getId())
                .shoppingSet(sc.getShoppingSet().stream().map(ShoppingCartSetDto::of).collect(Collectors.toSet()))
                .build();
    }

}
