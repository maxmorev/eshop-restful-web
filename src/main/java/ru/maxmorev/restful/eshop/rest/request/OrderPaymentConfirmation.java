package ru.maxmorev.restful.eshop.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Enums;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxmorev.restful.eshop.validation.CheckCustomerOrderId;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@JsonIgnoreProperties(ignoreUnknown = true)
@CheckCustomerOrderId(message = "{validation.order.payment.confirmation.invalid.orderId}")
public class OrderPaymentConfirmation {

    @NotNull
    private Long orderId;
    @NotBlank
    @NotNull
    private String paymentId;
    @NotBlank
    @NotNull
    private String paymentProvider;
    @JsonIgnore
    @AssertTrue(message = "{validation.order.payment.confirmation.unsupported.provider}")
    public boolean isPaymentProviderValid(){
        return Enums.getIfPresent(PaymentProvider.class, paymentProvider).isPresent();
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
