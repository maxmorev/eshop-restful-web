package ru.maxmorev.restful.eshop.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maxmorev.restful.eshop.services.CommodityService;

import javax.annotation.PostConstruct;

/**
 * Created by maxim.morev on 04/30/19.
 */
@Service
public class DBInitializer {

    private static Logger logger = LoggerFactory.getLogger(DBInitializer.class);
    CommodityService commodityService;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @PostConstruct
    public void initDB() {
        logger.info("Starting database initialization...");
        /*
        //testing part
        CommodityType type = new CommodityType();
        type.setDescription("Type description");
        type.setName("T-SHIRT");
        //commodityService.addType(type);
        */
        logger.info("Database initialization finished.");
    }

}
