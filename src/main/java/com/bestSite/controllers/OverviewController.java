package com.bestSite.controllers;

import com.bestSite.model.Comment;
import com.bestSite.model.Overview;
import com.bestSite.repository.CommentRepository;
import com.bestSite.repository.OverviewRepository;
import com.bestSite.repository.UserRepository;
import com.bestSite.service.OverviewService;
import com.bestSite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
public class OverviewController {

    private final OverviewService overviewService;
    private final OverviewRepository overviewRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Autowired
    public OverviewController(OverviewService overviewService, OverviewRepository overviewRepository, UserRepository userRepository,
                              UserService userService, CommentRepository commentRepository) {
        this.overviewService = overviewService;
        this.overviewRepository = overviewRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/overview")
    public String showOverviewPage(Model model) {
        Iterable<Overview> overviews = overviewRepository.findAll();
        model.addAttribute("overviews", overviews);
        return "overviews/overview";
    }

    @GetMapping("/overview/add")
    public String addOverview(Model model) {
        model.addAttribute("user",
                userRepository.findByUsername(userService.getCurrentUser().getName()).orElseThrow());
        return "overviews/overview-add";
    }
//
//    @PostMapping("/overview/add")
//    public String addOverview(@RequestParam String title, @RequestParam String image,
//                              @RequestParam String description, @RequestParam String text, Model model) {
//        overview = new Overview(title, image, description, text);
//        overview.setAuthor(userRepository.findByUsername(getCurrentUser().getName()).orElseThrow());
//        overviewRepository.save(overview);
//        return "redirect:/overview";
//    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/overview/add/{id}")
    public String addOverviewFromUser(@PathVariable(value = "id") long id, @ModelAttribute("overview") Overview overview,
                                      Model model) {
        model.addAttribute("user", userRepository.findById(id));
        return "overviews/overview-add";
    }

    //#user.username.equals(authentication.name)
    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/overview/add/{id}")
    public String addOverviewFromUser(@PathVariable(value = "id") long id, @ModelAttribute("overview") Overview overview,
                                      @RequestParam Optional<MultipartFile> newImage) {
        overviewService.saveOverview(id, overview, newImage);
        return "redirect:/overview";
    }

    @GetMapping("/overview/{id}")
    public String showDetail(@PathVariable(name = "id") long id, Model model) {
        if (overviewService.receiveData(id, model)) return "redirect:/overview";
        Iterable<Comment> comments = commentRepository.findByOverview(overviewRepository.findById(id).orElseThrow());
        model.addAttribute("comments", comments);
        return "overviews/overview-detail";
    }

    @PostMapping("/overview/{id}")
    public String addComment(@PathVariable(value = "id") long id, @RequestParam(name = "textComment") String text) {
        overviewService.saveComment(id, text);
        return "redirect:/overview/{id}";
    }

    @GetMapping("/overview/{id}/edit")
    public String edit(@PathVariable(name = "id") long id, Model model) {
        if (overviewService.receiveData(id, model)) return "redirect:/overview";
        return "overviews/overview-edit";
    }

    @PostMapping("/overview/{id}/edit")
    public String overviewUpdate(@PathVariable(name = "id") long id, @ModelAttribute("overview") Overview overview,
                                 @RequestParam Optional<MultipartFile> newImage) {
        overviewService.editOverview(id, overview, newImage);
        return "redirect:/overview";
    }

    @PostMapping("/overview/{id}/delete")
    public String overviewDelete(@PathVariable(name = "id") long id) {
        overviewService.deleteOverview(id);
        return "redirect:/overview";
    }

}
