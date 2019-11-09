package ru.maxmorev.restful.eshop.rest.response;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.data.domain.Page;
import ru.maxmorev.restful.eshop.entities.Commodity;

import java.util.List;

@Data
public class CommodityGrid {

    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<Commodity> commodityData;


    public CommodityGrid(Page<Commodity> commoditiesByPage){
        super();
        this.setCurrentPage(commoditiesByPage.getNumber() + 1);
        this.setTotalPages(commoditiesByPage.getTotalPages());
        this.setTotalRecords(commoditiesByPage.getTotalElements());
        this.setCommodityData(Lists.newArrayList(commoditiesByPage.iterator()));
    }

}
