package ru.maxmorev.restful.eshop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class OrderConfiguration {
    @Value("${order.expired.minutes}")
    private Integer orderExpiredMinutes;

}
