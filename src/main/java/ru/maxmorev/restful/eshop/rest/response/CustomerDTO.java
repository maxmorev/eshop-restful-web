package ru.maxmorev.restful.eshop.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO {
    private Long id;
    private String email;
    private String fullName;
    private String country;
    private String postcode;
    private String city;
    private String address;

    public static CustomerDTO of(CustomerInfo info){
        return CustomerDTO.builder()
                .id(info.getId())
                .email(info.getEmail())
                .fullName(info.getFullName())
                .country(info.getCountry())
                .city(info.getCity())
                .address(info.getAddress())
                .postcode(info.getPostcode())
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
