package ru.maxmorev.restful.eshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.maxmorev.restful.eshop.services.CommodityService;
import ru.maxmorev.restful.eshop.services.CommodityServiceImpl;
import ru.maxmorev.restful.eshop.services.ShoppingCartService;
import ru.maxmorev.restful.eshop.services.ShoppingCartServiceImpl;

/**
 * Created by maxim.morev on 04/30/19.
 */
@Configuration
@EnableJpaRepositories(basePackages = {"ru.maxmorev.restful.eshop.repos"})
@ComponentScan(basePackages = {"ru.maxmorev.restful.eshop.entities", "ru.maxmorev.restful.eshop.services", "ru.maxmorev.restful.eshop.rest.controllers", "ru.maxmorev.restful.eshop.web"})
public class ServiceConfig {

    @Bean
    CommodityService getCommodityService(){
        return new CommodityServiceImpl();
    }

    @Bean
    ShoppingCartService getShoppingCartService(){ return new ShoppingCartServiceImpl(); }

}