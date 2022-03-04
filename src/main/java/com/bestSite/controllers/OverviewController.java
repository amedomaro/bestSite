package com.bestSite.controllers;

import com.bestSite.model.Comment;
import com.bestSite.model.Overview;
import com.bestSite.model.User;
import com.bestSite.repository.CommentRepository;
import com.bestSite.repository.OverviewRepository;
import com.bestSite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final OverviewRepository overviewRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public OverviewController(OverviewRepository overviewRepository, UserRepository userRepository,
                              CommentRepository commentRepository) {
        this.overviewRepository = overviewRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

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
    public String addPostOverview(@RequestParam String title, @RequestParam String image,
                                  @RequestParam String description, @RequestParam String text) {
        Overview overview = new Overview(title, image, description, text);
        overviewRepository.save(overview);
        return "redirect:/overview";
    }

    @PostMapping("/overview/{id}")
    public String addComment(@PathVariable(value = "id") long id,
                             @RequestParam(name = "textComment") String text, Model model) {
        Overview overview = overviewRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(getCurrentUser().getName()).orElseThrow();
        Comment comment = new Comment(text, overview, user);
        commentRepository.save(comment);
        return "redirect:/overview/{id}";
    }

    @GetMapping("/overview/{id}/edit")
    public String edit(@PathVariable(name = "id") long id, Model model) {
        if (receiveData(id, model)) return "redirect:/overview";
        return "overview-edit";
    }

    @GetMapping("/overview/{id}")
    public String showDetail(@PathVariable(name = "id") long id, Model model) {
        if (receiveData(id, model)) return "redirect:/overview";
        Iterable<Comment> comments = commentRepository.findByOverview(overviewRepository.findById(id).orElseThrow());
        model.addAttribute("comments", comments);
        return "overview-detail";
    }

    @PostMapping("/overview/{id}/edit")
    public String overviewUpdate(@PathVariable(name = "id") long id, @RequestParam String title, @RequestParam String image,
                                 @RequestParam String description, @RequestParam String text) {
        Overview overview = overviewRepository.findById(id).orElseThrow();
        overview.setTitle(title);
        overview.setImage(image);
        overview.setDescription(description);
        overview.setText(text);
        overviewRepository.save(overview);
        return "redirect:/overview";
    }

    @PostMapping("/overview/{id}/delete")
    public String overviewDelete(@PathVariable(name = "id") long id, Model model) {
        Overview overview = overviewRepository.findById(id).orElseThrow();
        overviewRepository.delete(overview);
        return "redirect:/overview";
    }

    private boolean receiveData(@PathVariable(name = "id") long id, Model model) {
        if (!overviewRepository.existsById(id)) return true;             // check for ID
        Optional<Overview> overview = overviewRepository.findById(id);
        ArrayList<Overview> result = new ArrayList<>();
        overview.ifPresent(result::add);
        model.addAttribute("overview", result);
        return false;
    }

    private Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
