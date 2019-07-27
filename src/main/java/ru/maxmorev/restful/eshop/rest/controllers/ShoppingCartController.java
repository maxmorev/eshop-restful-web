package ru.maxmorev.restful.eshop.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.request.RequestShoppingCartSet;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;
import ru.maxmorev.restful.eshop.services.CommodityService;
import ru.maxmorev.restful.eshop.services.ShoppingCartService;

import java.util.Locale;
import java.util.Objects;

@RestController
public class ShoppingCartController {

    private final static Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    private CommodityService commodityService;
    private ShoppingCartService shoppingCartService;
    private MessageSource messageSource;

    @Autowired public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }
    @Autowired public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI+"shoppingCart/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ShoppingCart getShoppingCart( @PathVariable(name="id", required = true) Long id, Locale locale ) throws Exception{
        ShoppingCart sc = shoppingCartService.findShoppingCartById(id);
        logger.info("getShoppingCart -> " + sc);
        if(Objects.nonNull(sc)){
            return sc;
        }else {
            //TODO
            throw new IllegalArgumentException(messageSource.getMessage("shoppingCart.error.id", new Object[]{id}, locale));
        }
    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI+"shoppingCart/", method= RequestMethod.POST)
    @ResponseBody
    public ShoppingCart addToShoppingCartSet(@RequestBody RequestShoppingCartSet requestShoppingCartSet, Locale locale){
        logger.info("POST:> RequestShoppingCartSet :> " + requestShoppingCartSet);
        //TODO Validation
        //ShoppingCartSet shoppingCartSet = new ShoppingCartSet();

        CommodityBranch branch = commodityService.findBranchById(requestShoppingCartSet.getBranchId());
        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartById(requestShoppingCartSet.getShoppingCartId());
        ShoppingCartSet shoppingCartSet = shoppingCartService.findByBranchAndShoppingCart(branch, shoppingCart);
        if(Objects.isNull(shoppingCartSet)){
            shoppingCartSet = new ShoppingCartSet();
            shoppingCartSet.setAmount(requestShoppingCartSet.getAmount());
            shoppingCartSet.setBranch(branch);
            shoppingCartSet.setShoppingCart(shoppingCart);
        }
        return shoppingCartService.addToShoppingCartSet(shoppingCartSet, requestShoppingCartSet.getAmount());
    }
    //

    @RequestMapping(path = Constants.REST_PUBLIC_URI+"shoppingCart/", method= RequestMethod.DELETE)
    @ResponseBody
    public ShoppingCart removeFromShoppingCartSet(@RequestBody RequestShoppingCartSet requestShoppingCartSet, Locale locale){
        logger.info("DELETE:> RequestShoppingCartSet :> " + requestShoppingCartSet);
        //TODO Validation

        CommodityBranch branch = commodityService.findBranchById(requestShoppingCartSet.getBranchId());
        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartById(requestShoppingCartSet.getShoppingCartId());
        ShoppingCartSet shoppingCartSet = shoppingCartService.findByBranchAndShoppingCart(branch, shoppingCart);
        return shoppingCartService.removeFromShoppingCartSet( shoppingCartSet, requestShoppingCartSet.getAmount() );
    }



}
