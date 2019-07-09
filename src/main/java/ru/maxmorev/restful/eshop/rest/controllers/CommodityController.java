package ru.maxmorev.restful.eshop.rest.controllers;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.rest.request.RequestCommodity;
import ru.maxmorev.restful.eshop.rest.response.AbstractRestController;
import ru.maxmorev.restful.eshop.rest.response.CommodityGrid;
import ru.maxmorev.restful.eshop.rest.response.Message;
import ru.maxmorev.restful.eshop.entities.Commodity;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.services.CommodityService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestController
public class CommodityController extends AbstractRestController {

    private final static Logger logger = LoggerFactory.getLogger(CommodityController.class);

    private CommodityService commodityService;
    private MessageSource messageSource;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(path = "/commodity/", method=RequestMethod.POST)
    @ResponseBody
    public Message createCommodityFromRequset(@RequestBody @Valid RequestCommodity requestCommodity, BindingResult bindingResult, Locale locale){
        logger.info("POST -> createCommodityFromRequset");
        processBindingResult(bindingResult);
        commodityService.addCommodity(requestCommodity);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/commodity/", method = RequestMethod.PUT)
    @ResponseBody
    public Message updateCommodity(@RequestBody @Valid RequestCommodity requestCommodity, BindingResult bindingResult, Locale locale){
        logger.info("PUT -> updateCommodity");
        processBindingResult(bindingResult);
        commodityService.updateCommodity(requestCommodity);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/commodity/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Commodity getCommodity( @PathVariable(name="id", required = true) Long id, Locale locale ) throws Exception{
        Commodity cm = commodityService.findCommodityById(id);
        if(Objects.nonNull(cm)){
            return cm;
        }else {
            //
            throw new IllegalArgumentException(messageSource.getMessage("commodity.error.id", new Object[]{id}, locale));
        }
    }

    @RequestMapping(path = "/commodities/", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public CommodityGrid listCommodity(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows,
            @RequestParam(value = "sort", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order
    ) throws Exception{
        // Process order by
        Sort sort = null;
        String orderBy = sortBy;
        if (orderBy != null && orderBy.equals("dateOfCreation")) {
            orderBy = "dateOfCreation";
        }else{
            orderBy = "id";
        }
        if(Objects.isNull(order)){
            order = "desc";
        }
        if(Objects.isNull(page)){
            page = 1;
        }
        if(Objects.isNull(rows)){
            rows = 10;
        }

        if (orderBy != null && order != null) {
            if (order.equals("desc")) {
                sort = new Sort(Sort.Direction.DESC, orderBy);
            } else
                sort = new Sort(Sort.Direction.ASC, orderBy);
        }

        // Constructs page request for current page
        // Note: page number for Spring Data JPA starts with 0, while jqGrid starts with 1
        PageRequest pageRequest = null;

        if (sort != null) {
            pageRequest =  PageRequest.of(page - 1, rows, sort);
        } else {
            pageRequest = PageRequest.of(page - 1, rows);
        }

        Page<Commodity> commoditiesByPage = commodityService.findAllCommoditiesByPage(pageRequest);

        // Construct the grid data that will return as JSON data
        CommodityGrid commodityGrid = new CommodityGrid();

        commodityGrid.setCurrentPage(commoditiesByPage.getNumber() + 1);
        commodityGrid.setTotalPages(commoditiesByPage.getTotalPages());
        commodityGrid.setTotalRecords(commoditiesByPage.getTotalElements());

        commodityGrid.setCommodityData(Lists.newArrayList(commoditiesByPage.iterator()));

        return commodityGrid;
    }

    @RequestMapping( path = "/branches/", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityBranch> getBranches() throws Exception{
        return commodityService.findAllBranches();
    }

    @RequestMapping(path = "/commodityBranch/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommodityBranch getCommodityBranch( @PathVariable(name="id", required = true) Long branchId, Locale locale ) throws Exception{
        CommodityBranch branch = commodityService.findBranchById(branchId);
        if(Objects.nonNull(branch)){
            return branch;
        }else {
            throw new IllegalArgumentException(messageSource.getMessage("commodity.branch.error.id", new Object[]{branchId}, locale));
        }
    }




}
