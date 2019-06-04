package ru.maxmorev.restful.eshop.services;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.AttributeDataType;
import ru.maxmorev.restful.eshop.controllers.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.controllers.request.RequestCommodity;
import ru.maxmorev.restful.eshop.entities.*;
import ru.maxmorev.restful.eshop.repos.*;

import java.util.*;

@Service("commodityService")
@Transactional
public class CommodityServiceImpl implements CommodityService {

    private static final Logger logger = LoggerFactory.getLogger(CommodityServiceImpl.class);

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
        for(AttributeDataType dt:AttributeDataType.values()){
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
    @Transactional(readOnly = true)
    public CommodityType findTypeById(Long id) {
        return commodityTypeRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public CommodityType findTypeByName(String name) {
        return commodityTypeRepository.findByName(name).get();
    }

    @Override
    public void deleteTypeById(Long id) {
        commodityTypeRepository.deleteById(id);
    }

    private void createValue(CommodityAttribute prop, String valString){
        CommodityAttributeValue newValue = new CommodityAttributeValue(prop);
        Object val = newValue.createValueFrom( valString );
        newValue.setAttribute( prop);
        newValue.setValue( val );
        commodityAttributeValueRepository.save(newValue);
    }

    @Override
    public void addProperty(RequestAttributeValue property) {
        logger.info("Property : " + property);
        Optional<CommodityType> type = commodityTypeRepository.findById(property.getTypeId());
        if(!type.isPresent()){

            throw new IllegalArgumentException( "Type not found type.id=" + property.getTypeId());

        }
        Optional<CommodityAttribute> propertyOptional = commodityAttributeRepository.findByNameAndCommodityType(property.getName(), type.get());
        if(propertyOptional.isPresent()){
            //check: if value exist? true: do nothing esle create new value
            CommodityAttribute existProperty = propertyOptional.get();
            CommodityAttributeValue newValue = new CommodityAttributeValue(existProperty);
            Object val = newValue.createValueFrom( property.getValue() );
            if( existProperty.getValues().stream().noneMatch(v->v.getValue().equals(val))  ){
                //create value
                createValue(existProperty, property.getValue());
            }

        }else{
            CommodityAttribute newProperty = new CommodityAttribute();
            newProperty.setDataType(AttributeDataType.valueOf(property.getDataType()));
            newProperty.setName(property.getName());
            newProperty.setCommodityType(type.get());
            newProperty.setMeasure(property.getMeasure());
            commodityAttributeRepository.save(newProperty);
            createValue(newProperty, property.getValue());
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityAttribute> findPropertiesByTypeId(Long typeId) {
        return commodityAttributeRepository.findByCommodityType(commodityTypeRepository.findById(typeId).get());
    }

    @Override
    public void deletePropertyValueById(Long valueId) {
        Optional<CommodityAttributeValue> av =
                commodityAttributeValueRepository.findById(valueId);
        if(av.isPresent()){
            CommodityAttribute ca = av.get().getAttribute();

            logger.info("remove value "+ av.get().getValue() + " : " + ca.getValues().remove(av.get()));

            if(ca.getValues().isEmpty()){
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

    private Commodity createCommodityFromRequest(RequestCommodity requestCommodity){
        CommodityType commodityType;
        Optional<CommodityType> commodityTypeExist = commodityTypeRepository.findById( requestCommodity.getTypeId());

        if(commodityTypeExist.isPresent()){
            commodityType = commodityTypeExist.get();
        }else{
            throw new IllegalArgumentException("Illegal argument typeId=" + requestCommodity.getTypeId() );
        }

        logger.info("CREATE COMMODITY");
        Commodity commodity = new Commodity();
        commodity.setName(requestCommodity.getName());
        commodity.setShortDescription(requestCommodity.getShortDescription());
        commodity.setOverview(requestCommodity.getOverview());
        commodity.setType( commodityType );
        //commodityRepository.save( commodity );

        //create images of commodity
        List<CommodityImage> commodityImages = new ArrayList<>(4);
        for(String imageUrl: requestCommodity.getImages()){
            CommodityImage image = new CommodityImage();
            image.setUri(imageUrl);
            image.setCommodity(commodity);
            //commodityImageRepository.save( image );
            commodityImages.add( image );
        }
        commodity.setImages(commodityImages);
        return commodity;
    }

    private void createCommodityBranch(RequestCommodity requestCommodity, Commodity commodity){
        //create branch
        CommodityBranch commodityBranch = new CommodityBranch();
        commodityBranch.setAmount( requestCommodity.getAmount() );
        commodityBranch.setPrice( requestCommodity.getPrice() );
        commodityBranch.setCommodity(commodity);
        //commodityBranchRepository.save( commodityBranch );
        createBranchPropertySet(requestCommodity.getPropertyValues(), commodityBranch);
        commodity.getBranches().add(commodityBranch);
    }

    private boolean createPropertySet(CommodityBranchAttributeSet propertySet, Long valId){
        Optional<CommodityAttributeValue> commodityPropertyValueExist = commodityAttributeValueRepository.findById(valId);
        if(commodityPropertyValueExist.isPresent()) {
            CommodityAttributeValue commodityAttributeValue = commodityPropertyValueExist.get();
            propertySet.setAttribute(commodityAttributeValue.getAttribute());
            propertySet.setAttributeValue(commodityAttributeValue);
            //commodityBranchPropertySetRepository.save(propertySet);
            return true;
        }
        return false;
    }

    private void createBranchPropertySet(List<Long> valueIdList, CommodityBranch commodityBranch){
        //create branch properies
        for(Long propertyValueId: valueIdList){
            CommodityBranchAttributeSet propertySet = new CommodityBranchAttributeSet();
            propertySet.setBranch( commodityBranch );
            if(createPropertySet(propertySet, propertyValueId)) {
                commodityBranch.getAttributeSet().add(propertySet);
            }
        }
    }


    /**
     *
     * @param requestCommodity
     */

    @Override
    public void addCommodity(RequestCommodity requestCommodity) {
        logger.info("type : " + requestCommodity);
        Optional<Commodity> commodityExist = commodityRepository.findByNameAndType(requestCommodity.getName(), commodityTypeRepository.findById(requestCommodity.getTypeId()).get());
        if(commodityExist.isPresent()){
            //create new branch for existent commodity
            createCommodityBranch( requestCommodity, commodityExist.get());
            commodityRepository.save(commodityExist.get());

        }else{
            //create new commodity and dependent classes
            Commodity commodity = createCommodityFromRequest(requestCommodity);
            createCommodityBranch(requestCommodity, commodity);
            commodityRepository.save(commodity);
        }
    }

    @Override
    public void updateCommodity(RequestCommodity requestCommodity) {
        logger.info("RC : " + requestCommodity);
        Optional<CommodityBranch> commodityBranchOptional = commodityBranchRepository.findById(requestCommodity.getBranchId());
        if(commodityBranchOptional.isPresent()){

            CommodityBranch branch = commodityBranchOptional.get();
            List<CommodityBranchAttributeSet> propertySetList = new ArrayList<>(branch.getAttributeSet());
            //update branch propertySet
            boolean updateBranch = false;
            if(requestCommodity.getPropertyValues().size()>0) {
                //updateBranch = true;
                propertySetList.forEach(set->branch.getAttributeSet().remove(set));

                //TODO change attributeSet
                List<Long> valueIdList = requestCommodity.getPropertyValues();
                for(Long propertyValueId: valueIdList){
                    Optional<CommodityAttributeValue> commodityPropertyValueExist = commodityAttributeValueRepository.findById(propertyValueId);
                    if(commodityPropertyValueExist.isPresent()) {
                        CommodityAttributeValue commodityAttributeValue = commodityPropertyValueExist.get();

                        CommodityBranchAttributeSet newAttribute = new CommodityBranchAttributeSet();
                        newAttribute.setBranch( branch );
                        newAttribute.setAttribute(commodityAttributeValue.getAttribute());
                        newAttribute.setAttributeValue(commodityAttributeValue);
                        branch.getAttributeSet().add(newAttribute);
                        logger.info("Add newAttribute->" + newAttribute);
                    }
                }
                //createBranchPropertySet(requestCommodity.getPropertyValues(), branch);

            }
            logger.info("attributes updated: " + branch.getAttributeSet().size());
            branch.setAmount(requestCommodity.getAmount());
            branch.setPrice(requestCommodity.getPrice());
            //commodityBranchRepository.save(branch);
            logger.info("branch fields updated");
            Commodity commodity = branch.getCommodity();
            commodity.setShortDescription(requestCommodity.getShortDescription());
            commodity.setOverview(requestCommodity.getOverview());
            commodity.setName(requestCommodity.getName());
            List<CommodityImage> images = commodity.getImages();
            logger.info("requestCommodity.getImages().size() = " + requestCommodity.getImages().size());
            logger.info("images.size() = " + images.size() );
            if(requestCommodity.getImages().size()==images.size()) {
                for (int imgIdx = 0; imgIdx < requestCommodity.getImages().size(); imgIdx++) {
                    CommodityImage img = images.get(imgIdx);
                    logger.info("img.getUri()=" + img.getUri());
                    logger.info("requestCommodity.getImages().get(imgIdx)=" + requestCommodity.getImages().get(imgIdx));
                    if(!img.getUri().equals(requestCommodity.getImages().get(imgIdx))){
                        img.setUri(requestCommodity.getImages().get(imgIdx));
                        logger.info("save img");
                        //commodityImageRepository.save(img);
                    }
                }
            }
            logger.info("commodity fields updated");
            //commodityBranchRepository.save(branch);
            commodityRepository.save(commodity);

        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityBranch> findAllBranches() {
        return commodityBranchRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CommodityBranch findBranchById(Long branchId) {
        Optional<CommodityBranch>  commodityBranch = commodityBranchRepository.findById(branchId);
        if(commodityBranch.isPresent()) {
            return commodityBranch.get();
        }else{
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commodity> findAllCommodities() {
        return commodityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commodity> findAllCommoditiesByTypeName(String typeName) {

        Optional<CommodityType> typeExist = commodityTypeRepository.findByName(typeName);
        if(typeExist.isPresent()) {
            return commodityRepository.findByType(typeExist.get());
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Commodity findCommodityById(Long id) {
        Optional<Commodity> oc = commodityRepository.findById(id);
        if(oc.isPresent()){
            return oc.get();
        }else {
            return null;
        }
    }
}
