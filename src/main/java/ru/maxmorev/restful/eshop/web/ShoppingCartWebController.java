package ru.maxmorev.restful.eshop.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import ru.maxmorev.restful.eshop.annotation.ShoppingCookie;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.services.CustomerService;
import ru.maxmorev.restful.eshop.services.OrderPurchaseService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Controller
public class ShoppingCartWebController extends CommonWebController {

    private CustomerService customerService;
    private OrderPurchaseService orderPurchaseService;

    @Autowired public void setOrderPurchaseService(OrderPurchaseService orderPurchaseService) {
        this.orderPurchaseService = orderPurchaseService;
    }

    @Autowired public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path = {"/shopping/cart/"})
    public String getShoppingCart(
            HttpServletResponse response,
            @CookieValue(value = ShoppingCookie.SHOPPiNG_CART_NAME, required = false) Cookie cartCookie,
            Model uiModel) throws IOException {
        log.info("Listing shopping cart");
        addCommonAttributesToModel(uiModel);
        addShoppingCartAttributesToModel(cartCookie, response, uiModel);
        return "shopping/cart";
    }

    @GetMapping(path = {"/shopping/cart/checkout/"})
    public String proceedToCheckout(
            HttpServletResponse response,
            @CookieValue(value = ShoppingCookie.SHOPPiNG_CART_NAME, required = false) Cookie cartCookie,
            Model uiModel) throws IOException {
        log.info("Listing shopping cart and merge with customer");
        //merge shopping cart after login

        String id = getAuthenticationCustomerId();
        if(id==null)
            throw new IllegalAccessError("Not Authenticated");

        ShoppingCart scFromCookie = getShoppingCart(cartCookie, response);
        Customer authCustomer = customerService.findByEmail(id).get();

        scFromCookie = shoppingCartService.mergeCartFromCookieWithCustomer( scFromCookie, authCustomer);

        setShoppingCartCookie(scFromCookie, response);
        addCommonAttributesToModel(uiModel);
        uiModel.addAttribute("customer", authCustomer);
        uiModel.addAttribute(ShoppingCookie.SHOPPiNG_CART, scFromCookie );
        uiModel.addAttribute(ShoppingCookie.SHOPPiNG_CART_ITEMS_AMOUNT, scFromCookie.getItemsAmount() );

        //create transaction order and hold items for 10 minutes
        CustomerOrder order = orderPurchaseService.createOrderFor(authCustomer);
        uiModel.addAttribute("orderId", order.getId());

        return "shopping/proceedToCheckout";
    }



}
