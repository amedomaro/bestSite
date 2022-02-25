package com.bestSite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OverviewController {

    @GetMapping("/overview")
    public String showTestPage(Model model){
        model.addAttribute("title", "overview");
        return "overview";
    }
}
