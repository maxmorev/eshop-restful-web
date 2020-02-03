package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.maxmorev.restful.eshop.annotation.AuthorityValues;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "customer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends CustomerInfo implements UserDetails {

    @NotBlank(message = "{validation.customer.password}")
    @Column(nullable = false, length = 256)
    private String password;

    @JsonIgnore
    @Column(name = "verifycode", nullable = false, length = 256)
    private String verifyCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_of_creation", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date dateOfCreation;

    @Column
    private Boolean verified = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_CUSTOMER_SHOPPING_CART"))
    private ShoppingCart shoppingCart;

    @Column(name = "authorities", nullable = false)
    private String authorities;

    @Transient
    private Long shoppingCartId;

    @Builder
    public Customer(Long id, @NotBlank(message = "{validation.customer.email}") String email, @NotBlank(message = "{validation.customer.fullName}") String fullName, @NotBlank(message = "{validation.customer.country}") String country, @NotBlank(message = "{validation.customer.postcode}") String postcode, @NotBlank(message = "{validation.customer.city}") String city, @NotBlank(message = "{validation.customer.address}") String address, @NotBlank(message = "{validation.customer.password}") String password, String verifyCode, String authorities) {
        super(id, email, fullName, country, postcode, city, address);
        this.password = password;
        this.verifyCode = verifyCode;
        this.authorities = authorities;
    }

    //implementation methods of org.springframework.security.core.userdetails.UserDetails
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<CustomerAuthority> authSet = new HashSet<>();
        if (Objects.isNull(authorities))
            return null;
        Arrays.asList(authorities.split(","))
                .forEach(str ->
                        authSet.add(new CustomerAuthority(AuthorityValues.valueOf(str))));
        return authSet;
    }

    public void addAuthority(AuthorityValues auth) {
        if (Objects.isNull(authorities)) {
            authorities = auth.name();
            return;
        }
        Collection<? extends GrantedAuthority> authSet = getAuthorities();
        if (authSet.contains(new CustomerAuthority(auth))) return;
        authorities += "," + auth.name();
    }

    public void removeAllAuthorities() {
        this.authorities = null;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return verified;
    }

    @Override
    public boolean isAccountNonLocked() {
        return verified;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return verified;
    }

    @Override
    public boolean isEnabled() {
        return verified;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Customer)) return false;
        if (!super.equals(object)) return false;
        Customer customer = (Customer) object;
        return getPassword().equals(customer.getPassword()) &&
                java.util.Objects.equals(getVerifyCode(), customer.getVerifyCode()) &&
                getDateOfCreation().equals(customer.getDateOfCreation()) &&
                java.util.Objects.equals(getVerified(), customer.getVerified()) &&
                java.util.Objects.equals(getAuthorities(), customer.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPassword(), getVerifyCode(), getDateOfCreation(), getVerified());
    }
}
