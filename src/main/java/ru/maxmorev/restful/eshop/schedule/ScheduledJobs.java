package ru.maxmorev.restful.eshop.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.maxmorev.restful.eshop.services.OrderPurchaseService;

@Slf4j
@Component
@AllArgsConstructor
public class ScheduledJobs {

    private static final int INTERVAL = 1000*60;

    private final OrderPurchaseService orderPurchaseService;

    @Scheduled(fixedRate = INTERVAL)
    public void cleanExpiredOrders() {
        log.info(" cleanExpiredOrders ");
        orderPurchaseService.cleanExpiredOrders();
    }
}
