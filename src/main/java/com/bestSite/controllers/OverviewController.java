package com.bestSite.controllers;

import com.bestSite.model.Overview;
import com.bestSite.repository.OverviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OverviewController {

    @Autowired
    private OverviewRepository overviewRepository;

    @GetMapping("/overview")
    public String showOverviewPage(Model model) {
        Iterable<Overview> overviews = overviewRepository.findAll();
        model.addAttribute("overviews", overviews);
        model.addAttribute("title", "overview");
        return "overview";
    }

    @GetMapping("/overview/add")
    public String addOverview(Model model) {
        model.addAttribute("title", "new review");
        return "overviewAdd";
    }

    @PostMapping("/overview/add")
    public String addPostOverview(@RequestParam String title, @RequestParam String text) {
        Overview overview = new Overview(title, text);
        overviewRepository.save(overview);
        return "redirect:/overview";
    }
}
