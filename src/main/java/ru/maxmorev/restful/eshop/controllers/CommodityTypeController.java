package ru.maxmorev.restful.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.controllers.response.CrudResponse;
import ru.maxmorev.restful.eshop.entities.CommodityType;
import ru.maxmorev.restful.eshop.services.CommodityService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class CommodityTypeController {

    private static final Logger logger = Logger.getLogger(CommodityTypeController.class.getName());

    private CommodityService commodityService;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @RequestMapping(path = "/types/", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityType> getCommodityTypes() throws Exception{

        return commodityService.findAllTypes();

    }

    @RequestMapping(path = "/type/", method = RequestMethod.POST)
    @ResponseBody
    public CrudResponse createCommodityType(@RequestBody CommodityType type ){

        logger.info("type : " + type);

        commodityService.addType(type);

        return CrudResponse.OK;
    }

    @RequestMapping(path = "/type/", method = RequestMethod.PUT)
    @ResponseBody
    public CrudResponse updateCommodityType(@RequestBody CommodityType type ){
        commodityService.addType(type);
        return CrudResponse.OK;
    }


    @RequestMapping(path = "/type/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CrudResponse deleteCommodityType(@PathVariable(name = "id", required = true) Long id){
        commodityService.deleteTypeById(id);
        return CrudResponse.OK;
    }

    @RequestMapping(path = "/type/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommodityType getCommodityType(@PathVariable(name = "id", required = true) Long id){
        Optional<CommodityType> cmType = commodityService.findTypeById(id);
        if(cmType.isPresent()){
            return cmType.get();
        }else{
            StringBuilder errorMessage = new StringBuilder("Commodity Type with id=");
            errorMessage.append(id).append(" not found");
            throw new RuntimeException(errorMessage.toString());
        }
    }

}
