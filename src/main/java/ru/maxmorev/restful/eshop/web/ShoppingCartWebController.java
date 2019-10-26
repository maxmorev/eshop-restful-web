package ru.maxmorev.restful.eshop.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import ru.maxmorev.restful.eshop.annotation.ShoppingCookie;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;
import ru.maxmorev.restful.eshop.services.CustomerService;
import ru.maxmorev.restful.eshop.services.OrderPurchaseService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Controller
public class ShoppingCartWebController extends CommonWebController {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartWebController.class);

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
        logger.info("Listing shopping cart");
        addCommonAttributesToModel(uiModel);
        addShoppingCartAttributesToModel(cartCookie, response, uiModel);
        return "shopping/cart";
    }

    @GetMapping(path = {"/shopping/cart/checkout/"})
    public String proceedToCheckout(
            HttpServletResponse response,
            @CookieValue(value = ShoppingCookie.SHOPPiNG_CART_NAME, required = false) Cookie cartCookie,
            Model uiModel) throws IOException {
        logger.info("Listing shopping cart and merge with customer");
        //merge shopping cart after login

        String id = getAuthenticationCustomerId();
        if(id==null)
            throw new IllegalAccessError("Not Authenticated");

        ShoppingCart shoppingCart = getShoppingCart(cartCookie, response);
        logger.info("LOGGED IN CUSTOMER: " + id);
        logger.info("Shopping cart id: " + shoppingCart.getId());
        Customer customer = shoppingCart.getCustomer();
        logger.info("customer from shoppingCart: " + customer);
        Customer customerAuth = customerService.findByEmail(id);
        logger.info("customer from authContext: " + customerAuth.getId());
        if( Objects.nonNull(customerAuth.getShoppingCartId()) && !Objects.equals(customerAuth.getShoppingCart(),shoppingCart)){
            //merge cart from cookie to customer cart
            logger.info("merging cart");
            shoppingCart = shoppingCartService.mergeFromTo(shoppingCart, customerAuth.getShoppingCart());
            logger.info("update cookie");
            setShoppingCartCookie(shoppingCart, response);
        }
        if(Objects.isNull( customerAuth.getShoppingCartId() )){
            customerAuth.setShoppingCart(shoppingCart);
            customerService.update(customer);
        }
        shoppingCart = shoppingCartService.checkAvailability(shoppingCart);
        addCommonAttributesToModel(uiModel);
        uiModel.addAttribute("customer", customer);
        uiModel.addAttribute(ShoppingCookie.SHOPPiNG_CART, shoppingCart );

        uiModel.addAttribute(ShoppingCookie.SHOPPiNG_CART_ITEMS_AMOUNT, shoppingCart.getItemsAmount() );

        //TODO create transaction order and hold items for 10 minutes
        CustomerOrder order = orderPurchaseService.createOrderFor(customer);
        uiModel.addAttribute("orderId", order.getId());

        return "shopping/proceedToCheckout";
    }



}
