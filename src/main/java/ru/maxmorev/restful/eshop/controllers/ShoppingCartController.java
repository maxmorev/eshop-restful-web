package ru.maxmorev.restful.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.controllers.response.CrudResponse;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;
import ru.maxmorev.restful.eshop.services.ShoppingCartService;

import java.util.logging.Logger;

@RestController
public class ShoppingCartController {

    private static final Logger logger = Logger.getLogger(ShoppingCartController.class.getName());

    private ShoppingCartService shoppingCartService;

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }


    @RequestMapping(path = "/shoppingCart/", method= RequestMethod.POST)
    @ResponseBody
    public CrudResponse addToShoppingCartSet(@RequestBody ShoppingCartSet shoppingCartSet ){
        logger.info("POST:> createCommodityFromRequset :> " + shoppingCartSet);
        if(shoppingCartService.addToShoppingCartSet(shoppingCartSet)){
            return CrudResponse.OK;
        }
        return CrudResponse.FAIL;
    }
    //


}
