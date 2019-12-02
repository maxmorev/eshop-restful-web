package ru.maxmorev.restful.eshop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.maxmorev.restful.eshop.entities.*;
import ru.maxmorev.restful.eshop.rest.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.rest.request.RequestCommodity;

import java.util.List;
import java.util.Optional;

public interface CommodityService {

    List<String> getAvailebleAttributeDataTypes();

    List<CommodityType> findAllTypes(); //tested

    void addType(CommodityType type); //tested

    Optional<CommodityType> findTypeById(Long id); //tested

    Optional<CommodityType> findTypeByName(String name);

    void deleteTypeById(Long id); //tested

    void addAttribute(RequestAttributeValue attribute); //tested

    List<CommodityAttribute> findAttributesByTypeId(Long typeId); //tested

    void deleteAttributeValueById(Long valueId); //tested

    void addCommodity(RequestCommodity requestCommodity); //tested

    void updateCommodity(RequestCommodity requestCommodity ); //tested

    List<CommodityBranch> findAllBranches(); //tested

    Optional<CommodityBranch> findBranchById(Long branchId); //tested

    List<Commodity> findAllCommodities(); //tested
    Page<Commodity> findAllCommoditiesByPage(Pageable pageable);

    List<Commodity> findAllCommoditiesByTypeName(String typeName);//tested
    Optional<Commodity> findCommodityById(Long id);//tested


}
