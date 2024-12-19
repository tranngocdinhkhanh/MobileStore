package khanhtnd.mobilestore.service;

import khanhtnd.mobilestore.dto.request.AddProductRequestDto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductServiceAdvance {
    int addProduct(AddProductRequestDto productDto, List<MultipartFile> images);
}
