package ru.maxmorev.restful.eshop.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO {

    private String email;

    private String fullName;

    private String country;

    private String postcode;

    private String city;

    private String address;

    private CustomerDTO(){super();}

    public static CustomerDTO build(CustomerInfo info){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail(info.getEmail());
        customerDTO.setFullName(info.getFullName());
        customerDTO.setCountry(info.getCountry());
        customerDTO.setPostcode(info.getPostcode());
        customerDTO.setCity(info.getCity());
        customerDTO.setAddress(info.getAddress());
        return customerDTO;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
