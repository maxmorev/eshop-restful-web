package ru.maxmorev.restful.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.AttributeDataType;
import ru.maxmorev.restful.eshop.controllers.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.controllers.request.RequestCommodity;
import ru.maxmorev.restful.eshop.entities.*;
import ru.maxmorev.restful.eshop.repos.*;

import java.util.*;
import java.util.logging.Logger;

@Service("commodityService")
@Transactional
public class CommodityServiceImpl implements CommodityService {

    private static final Logger logger = Logger.getLogger(CommodityServiceImpl.class.getName());

    //Repositories
    private CommodityTypeRepository commodityTypeRepository;
    private CommodityPropertyRepository commodityPropertyRepository;
    private CommodityPropertyValueRepository commodityPropertyValueRepository;
    private CommodityRepository commodityRepository;
    private CommodityBranchRepository commodityBranchRepository;
    private CommodityBranchPropertySetRepository commodityBranchPropertySetRepository;
    private CommodityImageRepository commodityImageRepository;

    @Autowired
    public void setCommodityTypeRepository(CommodityTypeRepository commodityTypeRepository) {
        this.commodityTypeRepository = commodityTypeRepository;
    }
    @Autowired
    public void setCommodityPropertyRepository(CommodityPropertyRepository commodityPropertyRepository) {
        this.commodityPropertyRepository = commodityPropertyRepository;
    }
    @Autowired
    public void setCommodityPropertyValueRepository(CommodityPropertyValueRepository commodityPropertyValueRepository) {
        this.commodityPropertyValueRepository = commodityPropertyValueRepository;
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

    private void createValue(CommodityAttribute prop, String valString){
        CommodityAttributeValue newValue = new CommodityAttributeValue(prop);
        Object val = newValue.createValueFrom( valString );
        newValue.setAttributeId( prop.getId());
        newValue.setValue( val );
        commodityPropertyValueRepository.save(newValue);
    }

    @Override
    public void addProperty(RequestAttributeValue property) {
        logger.info("Property : " + property);
        Optional<CommodityAttribute> propertyOptional = commodityPropertyRepository.findByNameAndTypeId(property.getName(), property.getTypeId());
        if(propertyOptional.isPresent()){
            //check: if value exist? true: do nothing esle create new value
            CommodityAttribute existProperty = propertyOptional.get();
            CommodityAttributeValue newValue = new CommodityAttributeValue(existProperty);
            Object val = newValue.createValueFrom( property.getValue() );
            boolean createValue = !existProperty.getValues().stream().filter(v->v.getValue().equals(val)).findFirst().isPresent();
            if( createValue  ){
                //create value
                createValue(existProperty, property.getValue());
            }

        }else{
            CommodityAttribute newProperty = new CommodityAttribute();
            newProperty.setDataType(AttributeDataType.valueOf(property.getDataType()));
            newProperty.setName(property.getName());
            newProperty.setTypeId(property.getTypeId());
            newProperty.setMeasure(property.getMeasure());
            commodityPropertyRepository.save(newProperty);
            createValue(newProperty, property.getValue());
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityAttribute> findPropertiesByTypeId(Long typeId) {
        List<CommodityAttribute> properties = commodityPropertyRepository.findByTypeId(typeId);
        return properties;
    }

    @Override
    public void deletePropertyValueById(Long valueId) {
        Optional<CommodityAttributeValue> optionalCommodityPropertyValue =
                commodityPropertyValueRepository.findById(valueId);

        if(optionalCommodityPropertyValue.isPresent()){
            Optional<CommodityAttribute> optionalCommodityProperty =
                    commodityPropertyRepository.findById(optionalCommodityPropertyValue.get().getAttributeId());

            commodityPropertyValueRepository.deleteById(valueId);

            CommodityAttribute cp = optionalCommodityProperty.get();
            Optional<CommodityAttributeValue> optionalCPV = cp.getValues().stream().filter(v->v.getId().equals(valueId)).findFirst();
            if(optionalCPV.isPresent()){
                cp.getValues().remove(optionalCPV.get());
            }
            //check properties
            logger.info("check property: " + cp.toString());
            if(cp.getValues().isEmpty()){
                //delete empty property
                commodityPropertyRepository.delete(cp);
            }
            //return CrudResponse.OK;
        }
        //return CrudResponse.FAIL;
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
        commodity.setTypeId( commodityType.getId() );
        commodityRepository.save( commodity );

        //create images of commodity
        List<CommodityImage> commodityImages = new ArrayList<>(4);
        for(String imageUrl: requestCommodity.getImages()){
            CommodityImage image = new CommodityImage();
            image.setUri(imageUrl);
            image.setCommodityId(commodity.getId());
            commodityImageRepository.save( image );
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
        commodityBranch.setCommodityId(commodity.getId());
        commodityBranchRepository.save( commodityBranch );
        createBranchPropertySet(requestCommodity.getPropertyValues(), commodityBranch);
    }

    private boolean createPropertySet(CommodityBranchAttributeSet propertySet, Long valId){
        Optional<CommodityAttributeValue> commodityPropertyValueExist = commodityPropertyValueRepository.findById(valId);
        if(commodityPropertyValueExist.isPresent()) {
            CommodityAttributeValue commodityAttributeValue = commodityPropertyValueExist.get();
            propertySet.setAttributeId(commodityAttributeValue.getAttributeId());
            propertySet.setAttributeValueId(commodityAttributeValue.getId());
            commodityBranchPropertySetRepository.save(propertySet);
            return true;
        }
        return false;
    }

    private void createBranchPropertySet(List<Long> valueIdList, CommodityBranch commodityBranch){
        //create branch properies
        for(Long propertyValueId: valueIdList){
            CommodityBranchAttributeSet propertySet = new CommodityBranchAttributeSet();
            propertySet.setBranchId( commodityBranch.getId() );
            if(createPropertySet(propertySet, propertyValueId)) {
                if(Objects.isNull(commodityBranch.getPropertySet())) {
                    commodityBranch.setPropertySet(new ArrayList<>());
                }
                commodityBranch.getPropertySet().add(propertySet);
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
        Optional<Commodity> commodityExist = commodityRepository.findByNameAndTypeId(requestCommodity.getName(), requestCommodity.getTypeId());
        if(commodityExist.isPresent()){
            //create new branch for existent commodity
            /*
            TODO check: if there is a branch with identical set of properties
            if exist - send message, else create branch
            */

            createCommodityBranch( requestCommodity, commodityExist.get() );


        }else{
            //create new commodity and dependent classes
            Commodity commodity = createCommodityFromRequest(requestCommodity);
            createCommodityBranch(requestCommodity, commodity);
        }
    }

    @Override
    public void updateCommodity(RequestCommodity requestCommodity) {
        logger.info("RC : " + requestCommodity);
        Optional<CommodityBranch> commodityBranchOptional = commodityBranchRepository.findById(requestCommodity.getBranchId());
        if(commodityBranchOptional.isPresent()){

            CommodityBranch branch = commodityBranchOptional.get();
            List<CommodityBranchAttributeSet> propertySetList = branch.getPropertySet();
            //update branch propertySet
            if(requestCommodity.getPropertyValues().size()>0) {

                //check for update
                List<Long> listPropertyValue = requestCommodity.getPropertyValues();

                Collections.sort(listPropertyValue);
                Collections.sort(propertySetList, (ps1, ps2) -> {
                    if(ps1.getAttributeValueId()>ps2.getAttributeValueId()){
                        return 1;
                    }else {
                        return -1;
                    }
                });

                //equalize the lists by removing the excess
                int idx = 0;
                while(propertySetList.size()>listPropertyValue.size()){
                    CommodityBranchAttributeSet propertySet = propertySetList.get(idx);
                    if( !listPropertyValue.stream().filter( v->v.equals(propertySet.getAttributeValueId())).findFirst().isPresent() ){
                        commodityBranchPropertySetRepository.delete(propertySet);
                        propertySetList.remove(idx);
                    }

                    idx++;
                }
                List<Long> copyListPropertyValue = new ArrayList<>(listPropertyValue);
                int propIdx = 0;
                while ( copyListPropertyValue.size()>0 && propIdx<propertySetList.size() ) {
                    CommodityBranchAttributeSet propertySet = propertySetList.get(propIdx);
                    propIdx++;
                    if(copyListPropertyValue.size()>0 && !listPropertyValue.stream().filter( v->v.equals(propertySet.getAttributeValueId())).findFirst().isPresent()){
                        //if the property is absent in valuesId - change it to the first valId, which is missing in propertySetList
                        int validx=0;
                        logger.info("Values: " + listPropertyValue);
                        while (validx < listPropertyValue.size() ){
                            Long valId = listPropertyValue.get(validx);
                            logger.info("valueId="+valId);
                            if(!propertySetList.stream().filter(v->v.getAttributeValueId().equals(valId)).findFirst().isPresent()){
                                if(createPropertySet(propertySet, valId)) {
                                    logger.info("Remove " + valId +" from " + copyListPropertyValue);
                                    copyListPropertyValue.remove(valId);
                                    break;
                                }
                            }

                            validx++;
                        }

                    }else{
                        copyListPropertyValue.remove(propertySet.getAttributeValueId());
                    }

                }
                
                listPropertyValue = null;
                logger.info("Finally copyListPropertyValue is =" + copyListPropertyValue);
                if(copyListPropertyValue.size()>0)
                    createBranchPropertySet(copyListPropertyValue, branch);

                /*for (CommodityBranchAttributeSet propertySet : propertySetList) {
                    commodityBranchPropertySetRepository.delete(propertySet);
                }
                createBranchPropertySet(requestCommodity, branch);*/

            }


            boolean updateBranch = false;




            if(!branch.getAmount().equals(requestCommodity.getAmount())) {
                branch.setAmount(requestCommodity.getAmount());
                updateBranch = true;
            }
            if(!branch.getPrice().equals(requestCommodity.getPrice())) {
                branch.setPrice(requestCommodity.getPrice());
                updateBranch = true;
            }
            if(updateBranch) {
                commodityBranchRepository.save(branch);
            }


            Commodity commodity = branch.getCommodity();
            boolean updateCommodity = false;
            if(!commodity.getShortDescription().equals(requestCommodity.getShortDescription())) {
                commodity.setShortDescription(requestCommodity.getShortDescription());
                updateCommodity = true;
            }
            if( !commodity.getOverview().equals(requestCommodity.getOverview())) {
                commodity.setOverview(requestCommodity.getOverview());
                updateCommodity = true;
            }
            if( !commodity.getName().equals(requestCommodity.getName())) {
                commodity.setName(requestCommodity.getName());
                updateCommodity = true;
            }

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
                        commodityImageRepository.save(img);
                    }
                }

            }

            if(updateCommodity) {
                commodityRepository.save(commodity);
            }
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
            return commodityRepository.findByTypeId(typeExist.get().getId());
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
