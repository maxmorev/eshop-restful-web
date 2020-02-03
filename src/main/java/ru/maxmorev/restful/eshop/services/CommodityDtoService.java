package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.entities.CommodityType;
import ru.maxmorev.restful.eshop.rest.response.CommodityDto;
import ru.maxmorev.restful.eshop.rest.response.CommodityTypeDto;

import java.util.List;
import java.util.Optional;

public interface CommodityDtoService {

    List<CommodityDto> findWithBranchesAmountGt0();
    Optional<CommodityDto> findCommodityById(Long id);
    List<CommodityTypeDto> findAllTypes();
    List<CommodityDto> findWithBranchesAmountGt0AndType(String typeName);
    Optional<CommodityTypeDto> findTypeByName(String name);
}
