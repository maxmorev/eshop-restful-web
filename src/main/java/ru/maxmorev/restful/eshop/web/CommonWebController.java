package ru.maxmorev.restful.eshop.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import ru.maxmorev.restful.eshop.annotation.ShoppingCookie;
import ru.maxmorev.restful.eshop.entities.CommodityType;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.services.CommodityService;
import ru.maxmorev.restful.eshop.services.ShoppingCartService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;


public abstract class CommonWebController {

    private final Logger logger = LoggerFactory.getLogger(CommonWebController.class);

    protected ShoppingCartService shoppingCartService;

    protected CommodityService commodityService;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) { this.shoppingCartService = shoppingCartService; }

    /**
     * Add common attributes for UI Layer for header layout and so on
     * @param uiModel
     */
    protected void addCommonAttributesToModel(Model uiModel){
        List<CommodityType> typeList = commodityService.findAllTypes();
        uiModel.addAttribute("types", typeList);
    }

    private ShoppingCart setCookie(HttpServletResponse response){
        ShoppingCart shoppingCart = shoppingCartService.createEmptyShoppingCart();
        Cookie newCartCookie = new Cookie(ShoppingCookie.SHOPPiNG_CART_NAME, shoppingCart.getId().toString());
        newCartCookie.setComment("Shopping cart id for usability of our web shop UI. Thank you.");
        newCartCookie.setMaxAge(60*60*24*15);//15 days in seconds
        newCartCookie.setPath("/");
        //newCartCookie.setDomain();
        logger.info("SETTING COOKIE");
        response.addCookie(newCartCookie);
        return shoppingCart;
    }

    public ShoppingCart getShoppingCart(Cookie cartCookie, HttpServletResponse response){
        //work with shopping cart create new or find existing
        ShoppingCart shoppingCart;
        if(Objects.isNull(cartCookie)){
            // create new shopping cart and save cookie
            shoppingCart = setCookie(response);
        }else{
            // load shopping cart from shoppingCart service and add to uiModel
            logger.info("Load existing shopping cart: " + cartCookie.getValue());
            Long cartId = Long.valueOf( cartCookie.getValue() );
            shoppingCart = shoppingCartService.findShoppingCartById(cartId);
            if(Objects.isNull( shoppingCart )){
                //logger.info("Recreate shopping cart: " + cartCookie.getValue());
                shoppingCart = setCookie(response);
                logger.info("Recreate shopping cart: " + shoppingCart.getId() );
            }
        }
        return  shoppingCart;
    }

}
