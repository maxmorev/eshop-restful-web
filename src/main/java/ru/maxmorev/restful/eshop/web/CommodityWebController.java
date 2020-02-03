package ru.maxmorev.restful.eshop.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.maxmorev.restful.eshop.annotation.ShoppingCookie;
import ru.maxmorev.restful.eshop.rest.response.CommodityDto;
import ru.maxmorev.restful.eshop.rest.response.CommodityTypeDto;
import ru.maxmorev.restful.eshop.services.CommodityDtoService;
import ru.maxmorev.restful.eshop.services.ShoppingCartService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class CommodityWebController extends CommonWebController {

    public CommodityWebController(@Autowired ShoppingCartService shoppingCartService, @Autowired CommodityDtoService commodityDtoService) {
        super(shoppingCartService, commodityDtoService);
    }

    @GetMapping(path = {"/commodity/","/"})
    public String commodityList(
            HttpServletResponse response,
            @CookieValue(value = ShoppingCookie.SHOPPiNG_CART_NAME, required = false) Cookie cartCookie,
            Model uiModel){

        log.info("Listing branches");
        List<CommodityDto> commodities = commodityDtoService.findWithBranchesAmountGt0();
        addCommonAttributesToModel(uiModel);
        addShoppingCartAttributesToModel(cartCookie, response, uiModel);
        uiModel.addAttribute("commodities", commodities );
        log.info("No. of commodities: " + commodities.size());
        return "commodity/list";
    }

    @GetMapping(path = {"/commodity/type/{name}"})
    public String commodityListByType(
            HttpServletResponse response,
            @CookieValue(value = ShoppingCookie.SHOPPiNG_CART_NAME, required = false) Cookie cartCookie,
            @PathVariable(value = "name", required = true) String name,
            Model uiModel) {

        CommodityTypeDto type = commodityDtoService.findTypeByName(name)
                .orElseThrow(()->new IllegalArgumentException("Invalid type"));
        List<CommodityDto> commodities = commodityDtoService.findWithBranchesAmountGt0AndType(name);
        uiModel.addAttribute("currentType", type);
        addCommonAttributesToModel(uiModel);
        addShoppingCartAttributesToModel(cartCookie, response, uiModel);
        uiModel.addAttribute("commodities", commodities);
        return "commodity/list";
    }


    @GetMapping(path = "/commodity/{id}")
    public String commodity(
            HttpServletResponse response,
            @CookieValue(value = ShoppingCookie.SHOPPiNG_CART_NAME, required = false) Cookie cartCookie,
            @PathVariable(value = "id", required = true) Long id,
            Model uiModel) {
        Optional<CommodityDto> cm = commodityDtoService.findCommodityById(id);
        if(cm.isEmpty()){
            //TODO message like "The product you are looking for no longer exists."
            return "commodity/error-item";
        }
        uiModel.addAttribute("currentType", cm.get().getType());
        uiModel.addAttribute("commodity", cm.get());
        addCommonAttributesToModel(uiModel);
        addShoppingCartAttributesToModel(cartCookie, response, uiModel);
        //TODO improve this part and remove from the code the definition of special type of commodity "wear" t-shirt
        if(cm.get().getType().getName().toLowerCase().equals("t-shirt")){
            return "commodity/show-wear";
        }
        return "commodity/show-commodity";
    }




}
