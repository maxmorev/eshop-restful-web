package ru.maxmorev.restful.eshop.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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

    @Query("select c from Commodity c where c.id in (select cb.commodity.id\n" +
            "from CommodityBranch cb\n" +
            "where cb.amount > 0  \n" +
            "group by cb.commodity.id)")
    List<Commodity> findCommodityWithBranchesWithAmountGt0();

    @Query("select c from Commodity c where c.id in (select cb.commodity.id\n" +
            "from CommodityBranch cb\n" +
            "where cb.amount > 0  \n" +
            "group by cb.commodity.id) and c.type.name=:typeName")
    List<Commodity> findCommodityWithBranchesWithAmountGt0AndTypeName(@Param("typeName") String typeName);


}
