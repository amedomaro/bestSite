package com.bestSite.service;

import com.bestSite.Interface.PageInterface;
import com.bestSite.model.Page;
import com.bestSite.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class PageService implements PageInterface {

    private Page page;
    private final PageRepository pageRepository;
    private final CloudService cloudService;

    @Autowired
    public PageService(PageRepository pageRepository, CloudService cloudService) {
        this.pageRepository = pageRepository;
        this.cloudService = cloudService;
    }

    public boolean receiveData(@PathVariable(name = "id") long id, Model model) {
        if (!pageRepository.existsById(id)) return true;
        model.addAttribute("page", pageRepository.findById(id).orElseThrow());
        return false;
    }

    @Override
    public void update(long id, Page updatedPage, Optional<MultipartFile> newImage) {
        page = pageRepository.findById(id).orElseThrow();
        page.setName(updatedPage.getName());
        page.setText1(updatedPage.getText1());
        page.setText2(updatedPage.getText2());
        if (cloudService.fileIsPresent(newImage.orElseThrow())){
            String image = cloudService.uploadFile(newImage.orElseThrow());
//            if (page.getImage() != null) cloudService.deleteFile(page.getImage());
            page.setImage(image);
        }
        pageRepository.save(page);
    }

    @Override
    public void save(Page page, Optional<MultipartFile> newImage){
        String image = cloudService.uploadFile(newImage.orElseThrow());
        page.setImage(image);
        pageRepository.save(page);
    }
}
