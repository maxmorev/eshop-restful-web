package ru.maxmorev.restful.eshop.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private static final Logger logger = LoggerFactory.getLogger(CommonWebController.class);

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

    protected void addShoppingCartAttributesToModel(
            Cookie cartCookie,
            HttpServletResponse response,
            Model uiModel
    ){
        ShoppingCart shoppingCart = getShoppingCart(cartCookie, response);
        uiModel.addAttribute(ShoppingCookie.SHOPPiNG_CART_NAME, shoppingCart.getId());
        uiModel.addAttribute(ShoppingCookie.SHOPPiNG_CART_ITEMS_AMOUNT, shoppingCart.getItemsAmount());
        uiModel.addAttribute(ShoppingCookie.SHOPPiNG_CART, shoppingCart );
    }

    protected ShoppingCart setShoppingCartCookie(ShoppingCart shoppingCart, HttpServletResponse response){
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
            shoppingCart = setShoppingCartCookie(shoppingCartService.createEmptyShoppingCart(), response);
        }else{
            // load shopping cart from shoppingCart service and add to uiModel
            logger.info("Load existing shopping cart: " + cartCookie.getValue());
            Long cartId = Long.valueOf( cartCookie.getValue() );
            shoppingCart = shoppingCartService
                    .findShoppingCartById(cartId)
                    .orElse(setShoppingCartCookie(shoppingCartService.createEmptyShoppingCart(), response));
        }
        return  shoppingCart;
    }

    public String getAuthenticationCustomerId(){
        String id = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null)
            if (authentication.getPrincipal() instanceof UserDetails)
                id = ((UserDetails) authentication.getPrincipal()).getUsername();
            else if (authentication.getPrincipal() instanceof String)
                id = (String) authentication.getPrincipal();
        return id;
    }

}
