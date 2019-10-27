package ru.maxmorev.restful.eshop.rest.response;

import lombok.Data;
import ru.maxmorev.restful.eshop.entities.Commodity;

import java.util.List;

@Data
public class CommodityGrid {

    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<Commodity> commodityData;

}
