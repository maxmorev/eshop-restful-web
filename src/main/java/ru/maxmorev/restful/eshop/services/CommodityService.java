package ru.maxmorev.restful.eshop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.maxmorev.restful.eshop.controllers.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.controllers.request.RequestCommodity;
import ru.maxmorev.restful.eshop.entities.Commodity;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.CommodityAttribute;
import ru.maxmorev.restful.eshop.entities.CommodityType;

import java.util.List;
import java.util.Optional;

public interface CommodityService {

    List<String> getAvailebleAttributeDataTypes();

    List<CommodityType> findAllTypes(); //tested

    void addType(CommodityType type); //tested

    CommodityType findTypeById(Long id); //tested

    CommodityType findTypeByName(String name);

    void deleteTypeById(Long id); //tested

    void addProperty(RequestAttributeValue property); //tested

    List<CommodityAttribute> findPropertiesByTypeId(Long typeId); //tested

    void deletePropertyValueById(Long valueId); //tested

    void addCommodity(RequestCommodity requestCommodity); //tested

    void updateCommodity(RequestCommodity requestCommodity ); //tested

    List<CommodityBranch> findAllBranches(); //tested

    CommodityBranch findBranchById(Long branchId); //tested

    List<Commodity> findAllCommodities(); //tested
    Page<Commodity> findAllCommoditiesByPage(Pageable pageable);

    List<Commodity> findAllCommoditiesByTypeName(String typeName);
    Commodity findCommodityById(Long id);

}
