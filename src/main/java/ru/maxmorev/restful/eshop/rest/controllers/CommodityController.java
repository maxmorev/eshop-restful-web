package ru.maxmorev.restful.eshop.rest.controllers;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.entities.Commodity;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.request.RequestCommodity;
import ru.maxmorev.restful.eshop.rest.response.CommodityGrid;
import ru.maxmorev.restful.eshop.rest.response.Message;
import ru.maxmorev.restful.eshop.services.CommodityService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@RestController
public class CommodityController {

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

    @RequestMapping(path = Constants.REST_PRIVATE_URI+"commodity/", method=RequestMethod.POST)
    @ResponseBody
    public Message createCommodityFromRequset(@RequestBody @Valid RequestCommodity requestCommodity, Locale locale){
        log.info("POST -> createCommodityFromRequset");
        commodityService.addCommodity(requestCommodity);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = Constants.REST_PRIVATE_URI+"commodity/", method = RequestMethod.PUT)
    @ResponseBody
    public Message updateCommodity(@RequestBody @Valid RequestCommodity requestCommodity, Locale locale){
        log.info("PUT -> updateCommodity");
        commodityService.updateCommodity(requestCommodity);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI+"commodity/id/{id}", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Commodity getCommodity( @PathVariable(name="id", required = true) Long id, Locale locale ) throws Exception{
        return commodityService.findCommodityById(id)
                .orElseThrow(()->new IllegalArgumentException(messageSource.getMessage("commodity.error.id", new Object[]{id}, locale)));

    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI + "commodities/", method = RequestMethod.GET, produces="application/json")
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
                sort = Sort.by(Sort.Direction.DESC, orderBy);
            } else
                sort = Sort.by(Sort.Direction.ASC, orderBy);
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
        return new CommodityGrid(commoditiesByPage);
    }

    @RequestMapping( path = Constants.REST_PUBLIC_URI + "branches/", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityBranch> getBranches() throws Exception{
        return commodityService.findAllBranches();
    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI + "commodityBranch/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommodityBranch getCommodityBranch( @PathVariable(name="id", required = true) Long branchId, Locale locale ) throws Exception{
        return commodityService
                .findBranchById(branchId)
                .orElseThrow(()->new IllegalArgumentException(messageSource.getMessage("commodity.branch.error.id", new Object[]{branchId}, locale)));

    }




}
