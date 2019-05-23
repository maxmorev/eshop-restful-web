package ru.maxmorev.restful.eshop.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import ru.maxmorev.restful.eshop.annotation.ShoppingCookie;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ShoppingCartWebController extends CommonWebController {

    private final Logger logger = LoggerFactory.getLogger(ShoppingCartWebController.class);

    @GetMapping(path = {"/shopping/cart/"})
    public String getShoppingCart(
            HttpServletResponse response,
            @CookieValue(value = ShoppingCookie.SHOPPiNG_CART_NAME, required = false) Cookie cartCookie,
            Model uiModel) throws IOException {
        logger.info("Listing shopping cart");
        ShoppingCart shoppingCart = getShoppingCart(cartCookie, response);
        addCommonAttributesToModel(uiModel);
        uiModel.addAttribute("shoppingCart", shoppingCart );
        return "shopping/cart";
    }


}
