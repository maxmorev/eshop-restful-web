package ru.maxmorev.restful.eshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.maxmorev.restful.eshop.annotation.AuthorityValues;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Data
@Entity
@Table(name = "customer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends CustomerInfo implements UserDetails {

    @NotBlank(message = "{validation.customer.password}")
    @Column(nullable = false, length = 256)
    private String password;

    @JsonIgnore
    @Column(nullable = false, length = 256)
    private String verifyCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_of_creation", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date dateOfCreation;

    @Column
    private Boolean verified = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="shopping_cart_id", referencedColumnName = "id")
    private ShoppingCart shoppingCart;

    @Column(name = "authorities", nullable = false)
    private String authorities;

    @Transient
    private Long shoppingCartId;

    //implement methods of org.springframework.security.core.userdetails.UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<CustomerAuthority> authSet = new HashSet<>();
        if(Objects.isNull(authorities))
            return authSet;
        String[] splitAuth = authorities.split(",");
        for(String str: splitAuth){
            authSet.add(new CustomerAuthority(AuthorityValues.valueOf(str)));
        }
        return authSet;
    }

    public void addAuthority(AuthorityValues auth){
        if(Objects.isNull(authorities)){
            authorities = auth.name();
            return;
        }
        Collection<? extends GrantedAuthority> authSet = getAuthorities();
        if( authSet.contains( new CustomerAuthority(auth) ) ) return;
        authorities += ","+auth.name();
    }

    public void removeAllAuthorities(){
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

    public int hashCode() {
        return Objects.hash(super.hashCode(), getPassword(), getVerifyCode(), getDateOfCreation(), getVerified());
    }
}
