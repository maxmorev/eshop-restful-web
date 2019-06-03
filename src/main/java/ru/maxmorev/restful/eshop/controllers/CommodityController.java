package ru.maxmorev.restful.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.controllers.request.RequestCommodity;
import ru.maxmorev.restful.eshop.controllers.response.Message;
import ru.maxmorev.restful.eshop.entities.Commodity;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.services.CommodityService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestController
public class CommodityController {

    private final Logger logger = LoggerFactory.getLogger(CommodityController.class);

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

    @RequestMapping(path = "/commodity/", method=RequestMethod.POST)
    @ResponseBody
    public Message createCommodityFromRequset(@RequestBody @Valid RequestCommodity requestCommodity, BindingResult bindingResult, Locale locale){
        logger.info("POST -> createCommodityFromRequset");
        if (bindingResult.hasErrors()) {
            logger.info("!!!! bindingResult.hasErrors() !!!!");
            String errorContent = "";
            int index = 0;
            for(ObjectError error: bindingResult.getAllErrors()){
                errorContent += ++index+". " + error.getDefaultMessage()+"\n";
                logger.info(error.getDefaultMessage());
            }
            throw new RuntimeException( errorContent );
            //return new Message(Message.ERROR, "Error create commodity");
        }
        commodityService.addCommodity(requestCommodity);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/commodity/", method = RequestMethod.PUT)
    @ResponseBody
    public Message updateCommodity(@RequestBody RequestCommodity requestCommodity , Locale locale){
        logger.info("PUT -> updateCommodity");
        commodityService.updateCommodity(requestCommodity);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/commodity/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Commodity getCommodity( @PathVariable(name="id", required = true) Long id, Locale locale ) throws Exception{
        Commodity cm = commodityService.findCommodityById(id);
        if(Objects.nonNull(cm)){
            return cm;
        }else {
            //
            throw new IllegalArgumentException(messageSource.getMessage("commodity.error.id", new Object[]{id}, locale));
        }
    }

    @RequestMapping(path = "/commodities/", method = RequestMethod.GET)
    @ResponseBody
    public List<Commodity> listCommodity() throws Exception{
        return commodityService.findAllCommodities();
    }

    @RequestMapping( path = "/branches/", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityBranch> getBranches() throws Exception{
        return commodityService.findAllBranches();
    }

    @RequestMapping(path = "/commodityBranch/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommodityBranch getCommodityBranch( @PathVariable(name="id", required = true) Long branchId, Locale locale ) throws Exception{
        CommodityBranch branch = commodityService.findBranchById(branchId);
        if(Objects.nonNull(branch)){
            return branch;
        }else {
            throw new IllegalArgumentException(messageSource.getMessage("commodity.branch.error.id", new Object[]{branchId}, locale));
        }
    }




}
