package ru.maxmorev.restful.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.controllers.response.Message;
import ru.maxmorev.restful.eshop.entities.CommodityType;
import ru.maxmorev.restful.eshop.services.CommodityService;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestController
public class CommodityTypeController {

    private final Logger logger = LoggerFactory.getLogger(CommodityTypeController.class);

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

    @RequestMapping(path = "/types/", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityType> getCommodityTypes() throws Exception{

        return commodityService.findAllTypes();

    }

    @RequestMapping(path = "/type/", method = RequestMethod.POST)
    @ResponseBody
    public Message createCommodityType(@RequestBody CommodityType type, Locale locale ){
        logger.info("type : " + type);
        commodityService.addType(type);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/type/", method = RequestMethod.PUT)
    @ResponseBody
    public Message updateCommodityType(@RequestBody CommodityType type, Locale locale ){
        commodityService.addType(type);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }


    @RequestMapping(path = "/type/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Message deleteCommodityType(@PathVariable(name = "id", required = true) Long id, Locale locale){
        commodityService.deleteTypeById(id);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/type/{id}", method = RequestMethod.GET)
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
