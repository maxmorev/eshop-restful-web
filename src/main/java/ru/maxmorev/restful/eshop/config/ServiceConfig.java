package ru.maxmorev.restful.eshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.maxmorev.restful.eshop.services.CustomerService;
import ru.maxmorev.restful.eshop.services.CustomerServiceImpl;

/**
 * Created by maxim.morev on 04/30/19.
 */
@Configuration
@EnableJpaRepositories(basePackages = {"ru.maxmorev.restful.eshop.repository"})
@ComponentScan(basePackages = {"ru.maxmorev.restful.eshop.entities", "ru.maxmorev.restful.eshop.services", "ru.maxmorev.restful.eshop.rest.controllers", "ru.maxmorev.restful.eshop.web"})
public class ServiceConfig {

    private final CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();

    @Bean(name = { "customerService" })
    public CustomerService getCustomerService(){ return customerServiceImpl;}

    @Bean(name = {"userDetailsService"})
    UserDetailsService getUserDetailsService(){ return customerServiceImpl;}
}