package ru.maxmorev.restful.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.controllers.request.RequestShoppingCartSet;
import ru.maxmorev.restful.eshop.controllers.response.Message;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;
import ru.maxmorev.restful.eshop.services.CommodityService;
import ru.maxmorev.restful.eshop.services.ShoppingCartService;

import java.util.Locale;

@RestController
public class ShoppingCartController {

    private final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

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

    @RequestMapping(path = "/shoppingCart/", method= RequestMethod.POST)
    @ResponseBody
    public Message addToShoppingCartSet(@RequestBody RequestShoppingCartSet requestShoppingCartSet, Locale locale){
        logger.info("POST:> RequestShoppingCartSet :> " + requestShoppingCartSet);
        //TODO Validation
        ShoppingCartSet shoppingCartSet = new ShoppingCartSet();

        CommodityBranch branch = commodityService.findBranchById(requestShoppingCartSet.getBranchId());
        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartById(requestShoppingCartSet.getShoppingCartId());
        shoppingCartSet.setShoppingCart(shoppingCart);
        shoppingCartSet.setBranch(branch);
        shoppingCartSet.setAmount(requestShoppingCartSet.getAmount());
        if(shoppingCartService.addToShoppingCartSet(shoppingCartSet)){
            return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
        }
        return new Message(Message.ERROR, messageSource.getMessage("message_error", new Object[]{}, locale));
    }
    //


}
