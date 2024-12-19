package khanhtnd.mobilestore.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceAdvance {
    int saveImage(MultipartFile image);
}
