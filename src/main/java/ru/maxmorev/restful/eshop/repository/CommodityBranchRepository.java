package ru.maxmorev.restful.eshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;

import java.util.List;

@Repository
public interface CommodityBranchRepository extends CrudRepository<CommodityBranch, Long> {

    @Override
    List<CommodityBranch> findAll();
}
