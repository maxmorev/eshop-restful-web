package ru.maxmorev.restful.eshop.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminWebController {

    @GetMapping(path = {"/security/in/"})
    public String securityPage(Model uiModel){
        return "admin/login";
    }

    @GetMapping(path = {"/adm/"})
    public String mainPage(Model uiModel){
        return "admin/page";
    }
}
