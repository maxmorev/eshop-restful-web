package ru.maxmorev.restful.eshop.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.response.AbstractRestController;
import ru.maxmorev.restful.eshop.rest.response.Message;
import ru.maxmorev.restful.eshop.entities.CommodityType;
import ru.maxmorev.restful.eshop.services.CommodityService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestController
public class CommodityTypeController extends AbstractRestController {

    private final static Logger logger = LoggerFactory.getLogger(CommodityTypeController.class);

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

    @RequestMapping(path = Constants.REST_PUBLIC_URI+"types/", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityType> getCommodityTypes() throws Exception{

        return commodityService.findAllTypes();

    }

    @RequestMapping(path = Constants.REST_PRIVATE_URI + "type/", method = RequestMethod.POST)
    @ResponseBody
    public Message createCommodityType(@RequestBody @Valid CommodityType type, BindingResult bindingResult, Locale locale){
        logger.info("type : " + type);
        processBindingResult(bindingResult);
        commodityService.addType(type);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = Constants.REST_PRIVATE_URI + "type/", method = RequestMethod.PUT)
    @ResponseBody
    public Message updateCommodityType(@RequestBody @Valid CommodityType type, BindingResult bindingResult, Locale locale ){
        processBindingResult(bindingResult);
        commodityService.addType(type);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }


    @RequestMapping(path = Constants.REST_PRIVATE_URI + "type/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Message deleteCommodityType(@PathVariable(name = "id", required = true) Long id, Locale locale){
        commodityService.deleteTypeById(id);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI+"type/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommodityType getCommodityType(@PathVariable(name = "id", required = true) Long id, Locale locale){
        CommodityType cmType = commodityService.findTypeById(id);
        if(Objects.nonNull(cmType)){
            return cmType;
        }else{
            //messageSource.getMessage("commodity.branch.error.id", new Object[]{branchId}, locale)
            throw new IllegalArgumentException(messageSource.getMessage("commodity.type.error.id", new Object[]{id}, locale));
        }
    }

}
