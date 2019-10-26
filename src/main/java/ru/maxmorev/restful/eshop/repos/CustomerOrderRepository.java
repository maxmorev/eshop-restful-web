package ru.maxmorev.restful.eshop.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Long> {

    Optional<List<CustomerOrder>> findByCustomer(Customer customer);

}
