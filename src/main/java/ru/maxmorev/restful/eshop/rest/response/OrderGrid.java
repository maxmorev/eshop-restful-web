package ru.maxmorev.restful.eshop.rest.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class OrderGrid {

    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<CustomerOrderDto> orderData;


    public OrderGrid(Page<CustomerOrder> customerOrderPage) {
        this.setCurrentPage(customerOrderPage.getNumber() + 1);
        this.setTotalPages(customerOrderPage.getTotalPages());
        this.setTotalRecords(customerOrderPage.getTotalElements());
        this.setOrderData(customerOrderPage
                .stream()
                .map(CustomerOrderDto::of)
                .collect(Collectors.toList()));
    }
}
