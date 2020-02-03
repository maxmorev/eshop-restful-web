package ru.maxmorev.restful.eshop.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;

import java.util.List;

@Repository
public interface CommodityBranchRepository extends CrudRepository<CommodityBranch, Long> {

    @Override
    List<CommodityBranch> findAll();

    @Query("select cb from CommodityBranch cb where cb.amount > 0 and cb.commodity.id = :commodityId")
    List<CommodityBranch> findBranchesByIdWhereAmountGt0(@Param("commodityId") Long commodityId);


}
