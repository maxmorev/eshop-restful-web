package ru.maxmorev.restful.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.controllers.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.controllers.response.CrudResponse;
import ru.maxmorev.restful.eshop.entities.CommodityAttribute;
import ru.maxmorev.restful.eshop.services.CommodityService;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
public class CommodityAttributeController {

    private static final Logger logger = Logger.getLogger(CommodityAttributeController.class.getName());

    private CommodityService commodityService;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @RequestMapping(path = "/property/", method = RequestMethod.POST)
    @ResponseBody
    public CrudResponse createProperty(@RequestBody RequestAttributeValue property ){
        //to prevent duplicated properties
        //cleanup attribute meta
        property.setName( property.getName().toLowerCase().trim() );
        property.setValue( property.getValue().toLowerCase().trim() );
        if(Objects.nonNull( property.getMeasure()) ) {
            property.setMeasure(property.getMeasure().toLowerCase().trim());
        }
        logger.info("&&& -> RA is " + property);
        commodityService.addProperty(property);
        return CrudResponse.OK;
    }


    @RequestMapping(path = "/properties/{typeId}", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityAttribute> getProperties(@PathVariable(name = "typeId", required = true) Long typeId){
        return commodityService.findPropertiesByTypeId(typeId);
    }

    @RequestMapping(path = "/property/value/dataTypes/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAvailebleAttributeDataTypes(){
        return commodityService.getAvailebleAttributeDataTypes();
    }

    @RequestMapping(path = "/propertyValue/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CrudResponse deletePropertyValue(@PathVariable(name = "id", required = true) Long valueId){
        commodityService.deletePropertyValueById(valueId);
        return CrudResponse.OK;
    }



}
