package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfo extends AbstractEntity {
    private static final String emailRFC2822 = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    @Email(regexp = emailRFC2822, message = "{validation.customer.emailFormat}")
    @NotBlank(message = "{validation.customer.email}")
    @Column(nullable = false, length = 256, unique = true)
    private String email;

    @NotBlank(message = "{validation.customer.fullName}")
    @Column(name = "fullname", nullable = false, length = 256)
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

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CustomerInfo)) return false;
        if (!super.equals(object)) return false;
        CustomerInfo that = (CustomerInfo) object;
        return getEmail().equals(that.getEmail()) &&
                getFullName().equals(that.getFullName()) &&
                getCountry().equals(that.getCountry()) &&
                getPostcode().equals(that.getPostcode()) &&
                getCity().equals(that.getCity()) &&
                getAddress().equals(that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail(), getFullName(), getCountry(), getPostcode(), getCity(), getAddress());
    }
}
