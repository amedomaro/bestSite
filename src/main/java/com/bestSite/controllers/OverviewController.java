package com.bestSite.controllers;

import com.bestSite.model.Overview;
import com.bestSite.repository.OverviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

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
        return "overview-add";
    }

    @PostMapping("/overview/add")
    public String addPostOverview(@RequestParam String title, @RequestParam String text) {
        Overview overview = new Overview(title, text);
        overviewRepository.save(overview);
        return "redirect:/overview";
    }

    @GetMapping("/overview/{id}")
    public String showDetail(@PathVariable(name = "id") long id, Model model) {
        if (receiveData(id, model)) return "redirect:/overview";
        return "overview-detail";
    }

    @GetMapping("/overview/{id}/edit")
    public String edit(@PathVariable(name = "id") long id, Model model) {
        if (receiveData(id, model)) return "redirect:/overview";
        return "overview-edit";
    }

    @PostMapping("/overview/{id}/edit")
    public String overviewUpdate(@PathVariable(name = "id") long id, @RequestParam String title, @RequestParam String text, Model model) {
        Overview overview = overviewRepository.findById(id).orElseThrow();
        overview.setTitle(title);
        overview.setText(text);
        overviewRepository.save(overview);
        return "redirect:/overview";
    }

    private boolean receiveData(@PathVariable(name = "id") long id, Model model) {
        if (!overviewRepository.existsById(id)) return true;             // check for ID
        Optional<Overview> overview = overviewRepository.findById(id);
        ArrayList<Overview> result = new ArrayList<>();
        overview.ifPresent(result::add);
        model.addAttribute("post", result);
        return false;
    }
}
