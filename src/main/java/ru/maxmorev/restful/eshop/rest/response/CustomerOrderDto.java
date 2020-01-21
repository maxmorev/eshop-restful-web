package ru.maxmorev.restful.eshop.rest.response;

import lombok.Builder;
import lombok.Value;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class CustomerOrderDto {
    private Long customerId;
    private Date dateOfCreation;
    private CustomerOrderStatus status;
    private PaymentProvider paymentProvider;
    private String paymentID;
    private List<PurchaseDto> purchases;

    public static CustomerOrderDto of(CustomerOrder co) {
        return CustomerOrderDto.builder()
                .customerId(co.getCustomer().getId())
                .dateOfCreation(co.getDateOfCreation())
                .status(co.getStatus())
                .paymentProvider(co.getPaymentProvider())
                .paymentID(co.getPaymentID())
                .purchases(co.getPurchases()
                        .stream()
                        .sorted()
                        .map(PurchaseDto::of)
                        .collect(Collectors.toList()))
                .build();
    }

}
