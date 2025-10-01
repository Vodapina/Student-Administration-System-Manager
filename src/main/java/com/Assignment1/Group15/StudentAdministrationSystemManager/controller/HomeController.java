package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }


    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "redirect:/?logout";
    }
}