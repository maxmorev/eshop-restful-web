package ru.maxmorev.restful.eshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.AttributeDataType;
import ru.maxmorev.restful.eshop.entities.*;
import ru.maxmorev.restful.eshop.repository.*;
import ru.maxmorev.restful.eshop.rest.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.rest.request.RequestCommodity;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service("commodityService")
public class CommodityServiceImpl implements CommodityService {

    //Repositories
    private CommodityTypeRepository commodityTypeRepository;
    private CommodityAttributeRepository commodityAttributeRepository;
    private CommodityAttributeValueRepository commodityAttributeValueRepository;
    private CommodityRepository commodityRepository;
    private CommodityBranchRepository commodityBranchRepository;
    private CommodityBranchPropertySetRepository commodityBranchPropertySetRepository;
    private CommodityImageRepository commodityImageRepository;

    @Autowired
    public void setCommodityTypeRepository(CommodityTypeRepository commodityTypeRepository) {
        this.commodityTypeRepository = commodityTypeRepository;
    }

    @Autowired
    public void setCommodityAttributeRepository(CommodityAttributeRepository commodityAttributeRepository) {
        this.commodityAttributeRepository = commodityAttributeRepository;
    }

    @Autowired
    public void setCommodityAttributeValueRepository(CommodityAttributeValueRepository commodityAttributeValueRepository) {
        this.commodityAttributeValueRepository = commodityAttributeValueRepository;
    }

    @Autowired
    public void setCommodityRepository(CommodityRepository commodityRepository) {
        this.commodityRepository = commodityRepository;
    }

    @Autowired
    public void setCommodityBranchRepository(CommodityBranchRepository commodityBranchRepository) {
        this.commodityBranchRepository = commodityBranchRepository;
    }

    @Autowired
    public void setCommodityBranchPropertySetRepository(CommodityBranchPropertySetRepository commodityBranchPropertySetRepository) {
        this.commodityBranchPropertySetRepository = commodityBranchPropertySetRepository;
    }

    @Autowired
    public void setCommodityImageRepository(CommodityImageRepository commodityImageRepository) {
        this.commodityImageRepository = commodityImageRepository;
    }

    @Override
    public List<String> getAvailebleAttributeDataTypes() {
        List<String> types = new ArrayList<>();
        for (AttributeDataType dt : AttributeDataType.values()) {
            types.add(dt.name());
        }
        return types;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityType> findAllTypes() {
        return commodityTypeRepository.findAll();
    }

    @Override
    public void addType(CommodityType type) {
        commodityTypeRepository.save(type);
    }

    @Override
    public CommodityType updateType(CommodityType type) {
        return commodityTypeRepository
                .findById(type.getId())
                .map(t -> {
                    t.setName(type.getName());
                    t.setDescription(type.getDescription());
                    return commodityTypeRepository.save(t);
                })
                .orElseThrow(() -> new IllegalArgumentException("Type not found type.id=" + type.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommodityType> findTypeById(Long id) {
        return commodityTypeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommodityType> findTypeByName(String name) {
        return commodityTypeRepository.findByName(name);
    }

    @Override
    public void deleteTypeById(Long id) {
        commodityTypeRepository.deleteById(id);
    }

    private void createValue(CommodityAttribute prop, String valString) {
        CommodityAttributeValue newValue = new CommodityAttributeValue(prop);
        Object val = newValue.createValueFrom(valString);
        newValue.setAttribute(prop);
        newValue.setValue(val);
        commodityAttributeValueRepository.save(newValue);
    }

    @Override
    public void addAttribute(RequestAttributeValue attribute) {
        log.info("Property : {}", attribute);
        Optional<CommodityType> type = commodityTypeRepository.findById(attribute.getTypeId());
        if (!type.isPresent()) {

            throw new IllegalArgumentException("Type not found type.id=" + attribute.getTypeId());

        }
        Optional<CommodityAttribute> propertyOptional = commodityAttributeRepository.findByNameAndCommodityType(attribute.getName(), type.get());
        if (propertyOptional.isPresent()) {
            //check: if value exist? true: do nothing esle create new value
            CommodityAttribute existProperty = propertyOptional.get();
            CommodityAttributeValue newValue = new CommodityAttributeValue(existProperty);
            Object val = newValue.createValueFrom(attribute.getValue());
            if (existProperty.getValues().stream().noneMatch(v -> v.getValue().equals(val))) {
                //create value
                createValue(existProperty, attribute.getValue());
            }

        } else {
            CommodityAttribute newProperty = new CommodityAttribute();
            newProperty.setDataType(AttributeDataType.valueOf(attribute.getDataType()));
            newProperty.setName(attribute.getName());
            newProperty.setCommodityType(type.get());
            newProperty.setMeasure(attribute.getMeasure());
            commodityAttributeRepository.save(newProperty);
            createValue(newProperty, attribute.getValue());
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityAttribute> findAttributesByTypeId(Long typeId) {
        return commodityAttributeRepository.findByCommodityType(commodityTypeRepository.findById(typeId).get());
    }

    @Override
    public void deleteAttributeValueById(Long valueId) {
        Optional<CommodityAttributeValue> av =
                commodityAttributeValueRepository.findById(valueId);
        if (av.isPresent()) {
            CommodityAttribute ca = av.get().getAttribute();

            log.info("remove value {} : {}", av.get().getValue(), ca.getValues().remove(av.get()));

            if (ca.getValues().isEmpty()) {
                //delete empty property
                commodityAttributeRepository.delete(ca);
                return;
            }
            commodityAttributeRepository.save(ca);
        }

    }

    /*
        COMMODITY
     */
    private List<CommodityImage> createImageListOf(Commodity commodity, RequestCommodity requestCommodity){
        List<CommodityImage> commodityImages = new ArrayList<>(requestCommodity.getImages().size());
        log.info("requestCommodity.getImages() > {}", requestCommodity.getImages());
        Short imageIndex = 0;
        for (String imageUrl : requestCommodity.getImages()) {
            CommodityImage image = new CommodityImage();
            image.setImageOrder(imageIndex);
            image.setUri(imageUrl);
            image.setCommodity(commodity);
            //commodityImageRepository.save( image );
            commodityImages.add(image);
            imageIndex++;
        }
        return commodityImages;
    }

    private Commodity createCommodityFromRequest(RequestCommodity requestCommodity) {
        CommodityType commodityType;
        Optional<CommodityType> commodityTypeExist = commodityTypeRepository.findById(requestCommodity.getTypeId());

        if (commodityTypeExist.isPresent()) {
            commodityType = commodityTypeExist.get();
        } else {
            throw new IllegalArgumentException("Illegal argument typeId=" + requestCommodity.getTypeId());
        }

        log.info("CREATE COMMODITY");
        Commodity commodity = new Commodity();
        commodity.setName(requestCommodity.getName());
        commodity.setShortDescription(requestCommodity.getShortDescription());
        commodity.setOverview(requestCommodity.getOverview());
        commodity.setType(commodityType);
        //commodityRepository.save( commodity );
        commodity.getImages().addAll(createImageListOf(commodity, requestCommodity));
        //create images of commodity


        return commodity;
    }

    private void createCommodityBranch(RequestCommodity requestCommodity, Commodity commodity) {
        //create branch
        CommodityBranch commodityBranch = new CommodityBranch();
        commodityBranch.setAmount(requestCommodity.getAmount());
        commodityBranch.setPrice(requestCommodity.getPrice());
        commodityBranch.setCommodity(commodity);
        commodityBranch.setCurrency(Currency.getInstance(requestCommodity.getCurrencyCode()));
        //commodityBranchRepository.save( commodityBranch );
        createBranchPropertySet(requestCommodity.getPropertyValues(), commodityBranch);
        commodity.getBranches().add(commodityBranch);
    }

    private boolean createPropertySet(CommodityBranchAttributeSet propertySet, Long valId) {
        Optional<CommodityAttributeValue> commodityPropertyValueExist = commodityAttributeValueRepository.findById(valId);
        if (commodityPropertyValueExist.isPresent()) {
            CommodityAttributeValue commodityAttributeValue = commodityPropertyValueExist.get();
            propertySet.setAttribute(commodityAttributeValue.getAttribute());
            propertySet.setAttributeValue(commodityAttributeValue);
            //commodityBranchPropertySetRepository.save(propertySet);
            return true;
        }
        return false;
    }

    private void createBranchPropertySet(List<Long> valueIdList, CommodityBranch commodityBranch) {
        //create branch properies
        for (Long propertyValueId : valueIdList) {
            CommodityBranchAttributeSet propertySet = new CommodityBranchAttributeSet();
            propertySet.setBranch(commodityBranch);
            if (createPropertySet(propertySet, propertyValueId)) {
                commodityBranch.getAttributeSet().add(propertySet);
            }
        }
    }


    /**
     * @param requestCommodity
     */
    @Override
    public void addCommodity(RequestCommodity requestCommodity) {
        log.info("type : {}", requestCommodity);
        Optional<Commodity> commodityExist = commodityRepository.findByNameAndType(requestCommodity.getName(), commodityTypeRepository.findById(requestCommodity.getTypeId()).get());
        if (commodityExist.isPresent()) {
            //create new branch for existent commodity
            createCommodityBranch(requestCommodity, commodityExist.get());
            commodityRepository.save(commodityExist.get());

        } else {
            //create new commodity and dependent classes
            Commodity commodity = createCommodityFromRequest(requestCommodity);
            createCommodityBranch(requestCommodity, commodity);
            commodityRepository.save(commodity);
        }
    }

    @Override
    public void updateCommodity(RequestCommodity requestCommodity) {
        log.info("RequestCommodity : {}", requestCommodity);
        commodityBranchRepository.findById(requestCommodity.getBranchId()).ifPresent(branch -> {

            List<CommodityBranchAttributeSet> propertySetList = new ArrayList<>(branch.getAttributeSet());

            if (requestCommodity.getPropertyValues().size() > 0) {
                //updateBranch = true;
                propertySetList.forEach(set -> branch.getAttributeSet().remove(set));

                //TODO change attributeSet think bout refactorin
                List<Long> valueIdList = requestCommodity.getPropertyValues();
                for (Long propertyValueId : valueIdList) {
                    Optional<CommodityAttributeValue> commodityPropertyValueExist = commodityAttributeValueRepository.findById(propertyValueId);
                    commodityPropertyValueExist.ifPresent(commodityAttributeValue -> {
                                CommodityBranchAttributeSet newAttribute = new CommodityBranchAttributeSet();
                                newAttribute.setBranch(branch);
                                newAttribute.setAttribute(commodityAttributeValue.getAttribute());
                                newAttribute.setAttributeValue(commodityAttributeValue);
                                branch.getAttributeSet().add(newAttribute);
                                log.info("Add newAttribute -> {}", newAttribute);
                            }

                    );

                }

            }
            log.info("attributes updated: {}", branch.getAttributeSet().size());
            branch.setAmount(requestCommodity.getAmount());
            branch.setPrice(requestCommodity.getPrice());
            //commodityBranchRepository.save(branch);
            log.info("branch fields updated");
            Commodity commodity = branch.getCommodity();
            commodity.setShortDescription(requestCommodity.getShortDescription());
            commodity.setOverview(requestCommodity.getOverview());
            commodity.setName(requestCommodity.getName());
            commodity.getImages().forEach(commodityImage -> commodityImage.setCommodity(null));
            commodity.getImages().clear();
            commodity.getImages().addAll(createImageListOf(commodity, requestCommodity));
            //commodityBranchRepository.save(branch);
            log.info("CREATE IMAGE LIST 2");
            commodityRepository.save(commodity);

        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityBranch> findAllBranches() {
        return commodityBranchRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommodityBranch> findBranchById(Long branchId) {
        return commodityBranchRepository.findById(branchId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commodity> findAllCommodities() {
        return commodityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Commodity> findAllCommoditiesByPage(Pageable pageable) {
        return commodityRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commodity> findAllCommoditiesByTypeName(String typeName) {

        Optional<CommodityType> typeExist = commodityTypeRepository.findByName(typeName);
        if (typeExist.isPresent()) {
            return commodityRepository.findByType(typeExist.get());
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Commodity> findCommodityById(Long id) {
        return commodityRepository.findById(id);
    }

}
