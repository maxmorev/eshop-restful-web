package ru.maxmorev.restful.eshop.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Enums;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPaymentConfirmation extends OrderIdRequest {
    @NotBlank
    @NotNull
    private String paymentId;
    @NotBlank
    @NotNull
    private String paymentProvider;

    @JsonIgnore
    @AssertTrue(message = "{validation.order.payment.confirmation.unsupported.provider}")
    public boolean isPaymentProviderValid() {
        return Enums.getIfPresent(PaymentProvider.class, paymentProvider).isPresent();
    }

    @Builder
    public OrderPaymentConfirmation(@NotNull Long orderId, @NotBlank @NotNull String paymentId, @NotBlank @NotNull String paymentProvider) {
        super(orderId);
        this.paymentId = paymentId;
        this.paymentProvider = paymentProvider;
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
