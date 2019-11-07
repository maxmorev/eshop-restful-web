package ru.maxmorev.restful.eshop.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.entities.CommodityType;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.response.Message;
import ru.maxmorev.restful.eshop.services.CommodityService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
public class CommodityTypeController {

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
    public Message createCommodityType(@RequestBody @Valid CommodityType type, Locale locale){
        log.info("type : " + type);
        commodityService.addType(type);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = Constants.REST_PRIVATE_URI + "type/", method = RequestMethod.PUT)
    @ResponseBody
    public Message updateCommodityType(@RequestBody @Valid CommodityType type, Locale locale ){
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
        return commodityService.findTypeById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("commodity.type.error.id", new Object[]{id}, locale)));
    }

}
