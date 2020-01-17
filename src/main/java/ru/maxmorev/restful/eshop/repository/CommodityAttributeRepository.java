package ru.maxmorev.restful.eshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.entities.CommodityAttribute;
import ru.maxmorev.restful.eshop.entities.CommodityType;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommodityAttributeRepository extends CrudRepository<CommodityAttribute, Long> {

    @Override
    List<CommodityAttribute> findAll();

    Optional<CommodityAttribute> findByNameAndCommodityType(String name, CommodityType type);

    List<CommodityAttribute> findByCommodityType(CommodityType type);

}
