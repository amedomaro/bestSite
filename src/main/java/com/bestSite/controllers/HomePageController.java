package com.bestSite.controllers;

import com.bestSite.model.Page;
import com.bestSite.repository.PageRepository;
import com.bestSite.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class HomePageController {

    private final PageRepository pageRepository;
    private final PageService pageService;

    @Autowired
    public HomePageController(PageRepository pageRepository, PageService pageService) {
        this.pageRepository = pageRepository;
        this.pageService = pageService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
       model.addAttribute("page", pageRepository.findByName("homePage").orElseThrow());
        return "page/home";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") long id, Model model){
        if (pageService.receiveData(id, model)) return "redirect:/home";
        return "page/home-edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/edit")
    public String editPage(@PathVariable("id") long id, @ModelAttribute("page") @Valid Page page,
                           BindingResult bindingResult, @RequestParam Optional<MultipartFile> newImage){
        if(bindingResult.hasErrors()) return "page/home-edit";
        pageService.update(id, page, newImage);
        return "page/home";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/add")
    public String addPage(@ModelAttribute("page") Page page){
        return "page/page-add";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public String addPage(@ModelAttribute("page") @Valid Page page, BindingResult bindingResult,
                          @RequestParam Optional<MultipartFile> image){
        if(bindingResult.hasErrors()) return "page/page-add";
        pageService.save(page, image);
        return "page/home";
    }
}
