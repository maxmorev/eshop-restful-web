package ru.maxmorev.restful.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.controllers.request.RequestCommodity;
import ru.maxmorev.restful.eshop.controllers.response.CrudResponse;
import ru.maxmorev.restful.eshop.entities.Commodity;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.services.CommodityService;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
public class CommodityController {

    private static final Logger logger = Logger.getLogger(CommodityController.class.getName());

    private CommodityService commodityService;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @RequestMapping(path = "/commodity/", method=RequestMethod.POST)
    @ResponseBody
    public CrudResponse createCommodityFromRequset(@RequestBody RequestCommodity requestCommodity ){
        logger.info("POST -> createCommodityFromRequset");
        commodityService.addCommodity(requestCommodity);
        return CrudResponse.OK;
    }

    @RequestMapping(path = "/commodity/", method = RequestMethod.PUT)
    @ResponseBody
    public CrudResponse updateCommodity(@RequestBody RequestCommodity requestCommodity ){
        logger.info("PUT -> updateCommodity");
        commodityService.updateCommodity(requestCommodity);
        return CrudResponse.OK;
    }

    @RequestMapping(path = "/commodity/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Commodity getCommodity( @PathVariable(name="id", required = true) Long id ) throws Exception{
        Commodity cm = commodityService.findCommodityById(id);
        if(Objects.nonNull(cm)){
            return cm;
        }else {
            throw new IllegalArgumentException("Illegal parameter id=" + id);
        }
    }

    @RequestMapping(path = "/commodities/", method = RequestMethod.GET)
    @ResponseBody
    public List<Commodity> listCommodity() throws Exception{
        return commodityService.findAllCommodities();
    }

    @RequestMapping( path = "/branches/", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityBranch> getBranches() throws Exception{
        return commodityService.findAllBranches();
    }

    @RequestMapping(path = "/commodityBranch/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommodityBranch getCommodityBranch( @PathVariable(name="id", required = true) Long branchId ) throws Exception{
        CommodityBranch branch = commodityService.findBranchById(branchId);
        if(Objects.nonNull(branch)){
            return branch;
        }else {
            throw new IllegalArgumentException("Illegal parameter id=" + branchId);
        }
    }




}
