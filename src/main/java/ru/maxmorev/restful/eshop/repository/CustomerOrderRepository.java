package ru.maxmorev.restful.eshop.repository;

import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerOrderRepository  extends PagingAndSortingRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByCustomer(Customer customer);
    //@Query("select co from CustomerOrder co where co.dateOfCreation < :expiredDate and co.status=:status")
    Optional<CustomerOrder> findByIdAndCustomerId(Long id, Long customerId);
    List<CustomerOrder> findByCustomerOrderByDateOfCreationDesc(Customer customer);
    List<CustomerOrder> findByCustomerAndStatusOrderByDateOfCreationDesc(Customer customer, CustomerOrderStatus status);
    List<CustomerOrder> findByCustomerIdAndStatusNotOrderByDateOfCreationDesc(Long id, CustomerOrderStatus status);
    @Query("select co from CustomerOrder co where co.dateOfCreation < :expiredDate and co.status=:status")
    List<CustomerOrder> findExpiredOrdersByStatus(@Param("status") CustomerOrderStatus status, @Param("expiredDate") Date expiredDate);

    Page<CustomerOrder> findByStatusNot(Pageable pageable, CustomerOrderStatus status);

    Page<CustomerOrder> findByStatus(Pageable pageable, CustomerOrderStatus status);
}
