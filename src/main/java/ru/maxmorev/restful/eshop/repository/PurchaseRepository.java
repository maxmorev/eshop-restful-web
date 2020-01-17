package ru.maxmorev.restful.eshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.entities.Purchase;
import ru.maxmorev.restful.eshop.entities.PurchaseId;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, PurchaseId> {
}
