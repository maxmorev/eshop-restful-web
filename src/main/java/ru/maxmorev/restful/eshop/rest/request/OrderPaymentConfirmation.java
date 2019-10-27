package ru.maxmorev.restful.eshop.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Enums;
import lombok.Data;
import ru.maxmorev.restful.eshop.validation.CheckCustomerOrderId;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
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
    public boolean isCommodityAttributeDataTypeValid(){
        return Enums.getIfPresent(PaymentProvider.class, paymentProvider).isPresent();
    }

}
