package ru.maxmorev.restful.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.controllers.response.Message;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;
import ru.maxmorev.restful.eshop.services.ShoppingCartService;

import java.util.Locale;
import java.util.logging.Logger;

@RestController
public class ShoppingCartController {

    private static final Logger logger = Logger.getLogger(ShoppingCartController.class.getName());

    private ShoppingCartService shoppingCartService;
    private MessageSource messageSource;

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(path = "/shoppingCart/", method= RequestMethod.POST)
    @ResponseBody
    public Message addToShoppingCartSet(@RequestBody ShoppingCartSet shoppingCartSet, Locale locale){
        logger.info("POST:> createCommodityFromRequset :> " + shoppingCartSet);
        if(shoppingCartService.addToShoppingCartSet(shoppingCartSet)){
            return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
        }
        return new Message(Message.ERROR, messageSource.getMessage("message_error", new Object[]{}, locale));
    }
    //


}
