package ru.maxmorev.restful.eshop.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;

/**
 * The POJO class to build from the JSON request with id's for persistence level class ShoppingCartSet
 * @see ShoppingCartSet
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestShoppingCartSet {
    //TODO Validation
    private Long shoppingCartId;
    private Long branchId;
    private Integer amount;

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
