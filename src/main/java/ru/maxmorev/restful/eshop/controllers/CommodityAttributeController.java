package ru.maxmorev.restful.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.controllers.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.controllers.response.Message;
import ru.maxmorev.restful.eshop.entities.CommodityAttribute;
import ru.maxmorev.restful.eshop.services.CommodityService;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
public class CommodityAttributeController {

    private static final Logger logger = Logger.getLogger(CommodityAttributeController.class.getName());

    private CommodityService commodityService;
    private MessageSource messageSource;


    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(path = "/property/", method = RequestMethod.POST)
    @ResponseBody
    public Message createProperty(@RequestBody RequestAttributeValue property, Locale locale ){
        //to prevent duplicated properties
        //cleanup attribute meta
        property.setName( property.getName().toLowerCase().trim() );
        property.setValue( property.getValue().toLowerCase().trim() );
        if(Objects.nonNull( property.getMeasure()) ) {
            property.setMeasure(property.getMeasure().toLowerCase().trim());
        }
        logger.info("&&& -> RA is " + property);
        commodityService.addProperty(property);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
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
    public Message deletePropertyValue(@PathVariable(name = "id", required = true) Long valueId, Locale locale){
        commodityService.deletePropertyValueById(valueId);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }



}
