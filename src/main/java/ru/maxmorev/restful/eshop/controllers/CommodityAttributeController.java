package ru.maxmorev.restful.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.controllers.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.controllers.response.AbstractRestController;
import ru.maxmorev.restful.eshop.controllers.response.Message;
import ru.maxmorev.restful.eshop.entities.CommodityAttribute;
import ru.maxmorev.restful.eshop.services.CommodityService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestController
public class CommodityAttributeController extends AbstractRestController {

    private final static Logger logger = LoggerFactory.getLogger(CommodityAttributeController.class);

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
    public Message createProperty(@RequestBody @Valid RequestAttributeValue property, BindingResult bindingResult, Locale locale ){
        //to prevent duplicated properties
        logger.info("&&& -> RA is " + property);
        processBindingResult(bindingResult);
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
