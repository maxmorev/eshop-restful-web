package ru.maxmorev.restful.eshop.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByCustomer(Customer customer);
    @Query("select co from CustomerOrder co where co.dateOfCreation < :expiredDate and co.status=:status")
    List<CustomerOrder> findExpiredOrdersByStatus(@Param("status") CustomerOrderStatus status, @Param("expiredDate") Date expiredDate);

}
