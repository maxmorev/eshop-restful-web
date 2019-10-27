package ru.maxmorev.restful.eshop.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.maxmorev.restful.eshop.annotation.ShoppingCookie;
import ru.maxmorev.restful.eshop.rest.response.CustomerDTO;
import ru.maxmorev.restful.eshop.services.CustomerService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class CustomerWebController extends CommonWebController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerWebController.class);

    private CustomerService customerService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path = {"/customer/account/create/", "/customer/account/create/{from}/"})
    public String createAccount(
            @PathVariable(name = "from", required = false) Optional<String> from,
            HttpServletResponse response,
            @CookieValue(value = ShoppingCookie.SHOPPiNG_CART_NAME, required = false) Cookie cartCookie,
            Model uiModel) throws IOException {

        addCommonAttributesToModel(uiModel);
        addShoppingCartAttributesToModel(cartCookie, response, uiModel);
        if(from.isPresent()){
            uiModel.addAttribute("fromPage", from.get());
        }
        return "customer/createAccount";
    }

    @GetMapping(path = {"/customer/account/update/"})
    public String updateAccount(
            HttpServletResponse response,
            @CookieValue(value = ShoppingCookie.SHOPPiNG_CART_NAME, required = false) Cookie cartCookie,
            Model uiModel) throws IOException {

        addCommonAttributesToModel(uiModel);
        addShoppingCartAttributesToModel(cartCookie, response, uiModel);
        String id = getAuthenticationCustomerId();
        CustomerDTO customerDTO = CustomerDTO.of(customerService.findByEmail(id));
        logger.info("CUSTOMER: " + customerDTO);
        uiModel.addAttribute("customer", customerDTO );
        return "customer/updateAccount";
    }
}
