package ru.maxmorev.restful.eshop.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.entities.Commodity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommodityRepository extends CrudRepository<Commodity, Long> {

    Optional<Commodity> findByNameAndTypeId(String name, Long typeId);
    @Override
    List<Commodity> findAll();

    List<Commodity> findByTypeId(Long typeId);

}
