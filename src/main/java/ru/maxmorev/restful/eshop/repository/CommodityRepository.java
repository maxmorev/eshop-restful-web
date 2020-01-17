package ru.maxmorev.restful.eshop.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.entities.Commodity;
import ru.maxmorev.restful.eshop.entities.CommodityType;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommodityRepository extends PagingAndSortingRepository<Commodity, Long> {

    Optional<Commodity> findByNameAndType(String name, CommodityType type);
    @Override List<Commodity> findAll();
    List<Commodity> findByType(CommodityType type);

}
