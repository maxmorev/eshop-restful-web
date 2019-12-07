package ru.maxmorev.restful.eshop.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO {

    private String email;

    private String fullName;

    private String country;

    private String postcode;

    private String city;

    private String address;

    public static CustomerDTO of(CustomerInfo info){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail(info.getEmail());
        customerDTO.setFullName(info.getFullName());
        customerDTO.setCountry(info.getCountry());
        customerDTO.setPostcode(info.getPostcode());
        customerDTO.setCity(info.getCity());
        customerDTO.setAddress(info.getAddress());
        return customerDTO;

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
