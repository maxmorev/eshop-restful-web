package ru.maxmorev.restful.eshop.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.entities.CommodityAttribute;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommodityPropertyRepository extends CrudRepository<CommodityAttribute, Long> {

    @Override
    List<CommodityAttribute> findAll();

    Optional<CommodityAttribute> findByNameAndTypeId(String name, Long typeId);

    List<CommodityAttribute> findByTypeId(Long typeId);

}
