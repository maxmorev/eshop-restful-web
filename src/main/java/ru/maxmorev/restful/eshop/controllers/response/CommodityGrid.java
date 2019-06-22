package ru.maxmorev.restful.eshop.controllers.response;

import ru.maxmorev.restful.eshop.entities.Commodity;

import java.util.List;

public class CommodityGrid {

    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<Commodity> commodityData;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<Commodity> getCommodityData() {
        return commodityData;
    }

    public void setCommodityData(List<Commodity> commodityData) {
        this.commodityData = commodityData;
    }
}
