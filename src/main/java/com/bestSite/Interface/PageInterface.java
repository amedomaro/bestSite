package com.bestSite.Interface;

import com.bestSite.model.Page;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

public interface PageInterface {

    void save(Page page, Optional<MultipartFile> image);
    void update(long id, Page page, Optional<MultipartFile> image);
}
