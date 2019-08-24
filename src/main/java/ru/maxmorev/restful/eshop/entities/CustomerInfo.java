package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@MappedSuperclass
public class CustomerInfo extends AbstractEntity {

    @NotBlank(message = "{validation.customer.email}")
    @Column(nullable = false, length = 256, unique = true)
    private String email;

    @NotBlank(message = "{validation.customer.fullName}")
    @Column(nullable = false, length = 256)
    private String fullName;

    @NotBlank(message = "{validation.customer.country}")
    @Column(nullable = false, length = 256)
    private String country;

    @NotBlank(message = "{validation.customer.postcode}")
    @Column(nullable = false, length = 256)
    private String postcode;

    @NotBlank(message = "{validation.customer.city}")
    @Column(nullable = false, length = 256)
    private String city;

    @NotBlank(message = "{validation.customer.address}")
    @Column(nullable = false, length = 256)
    private String address;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerInfo)) return false;
        if (!super.equals(o)) return false;
        CustomerInfo that = (CustomerInfo) o;
        return email.equals(that.email) &&
                fullName.equals(that.fullName) &&
                country.equals(that.country) &&
                postcode.equals(that.postcode) &&
                city.equals(that.city) &&
                address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, fullName, country, postcode, city, address);
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
