package ru.maxmorev.restful.eshop.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.rest.response.Message;
import ru.maxmorev.restful.eshop.entities.CommodityAttribute;
import ru.maxmorev.restful.eshop.services.CommodityService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
public class CommodityAttributeController {

    private final static Logger logger = LoggerFactory.getLogger(CommodityAttributeController.class);
    public static final String ATTRIBUTE_DATA_TYPES = "/attribute/value/dataTypes/";

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

    @RequestMapping(path = Constants.REST_PRIVATE_URI+"attribute/", method = RequestMethod.POST)
    @ResponseBody
    public Message createAttribute(@RequestBody @Valid RequestAttributeValue property, Locale locale ){
        //to prevent duplicated properties
        commodityService.addAttribute(property);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI+"attributes/{typeId}", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityAttribute> getAttributes(@PathVariable(name = "typeId", required = true) Long typeId){
        return commodityService.findAttributesByTypeId(typeId);
    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI+ATTRIBUTE_DATA_TYPES, method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAvailebleAttributeDataTypes(){
        return commodityService.getAvailebleAttributeDataTypes();
    }

    @RequestMapping(path = Constants.REST_PRIVATE_URI+"attributeValue/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Message deletePropertyValue(@PathVariable(name = "id", required = true) Long valueId, Locale locale){
        commodityService.deleteAttributeValueById(valueId);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }



}
