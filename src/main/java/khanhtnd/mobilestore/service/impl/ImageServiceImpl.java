package khanhtnd.mobilestore.service.impl;

import khanhtnd.mobilestore.dto.response.PageDto;
import khanhtnd.mobilestore.exception.common.InvalidImageException;
import khanhtnd.mobilestore.exception.common.NotFoundException;
import khanhtnd.mobilestore.model.Image;
import khanhtnd.mobilestore.repository.ImageRepository;
import khanhtnd.mobilestore.service.CommonService;
import khanhtnd.mobilestore.service.ImageServiceAdvance;
import khanhtnd.mobilestore.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageServiceImpl implements CommonService<Image>, ImageServiceAdvance {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public int saveImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return -1; // Trả về giá trị mặc định nếu không có ảnh hợp lệ
        }

        try {
            // Tạo thực thể Image từ file
            Image newImage = new Image();
            newImage.setName(image.getOriginalFilename());
            newImage.setType(image.getContentType());
            newImage.setData(image.getBytes());

            // Lưu ảnh vào cơ sở dữ liệu
            Image savedImage = imageRepository.save(newImage);

            // Trả về ID của ảnh
            return savedImage.getId();
        } catch (IOException e) {
            throw new InvalidImageException(Message.MSG_101);
        }
    }

    @Override
    public Image getById(int id) {
        Optional<Image> imageOptional = imageRepository.findById(id);
        return imageOptional.orElseThrow(() -> new NotFoundException(Message.MSG_103, id));
    }

    @Override
    public PageDto<Image> getAll(Pageable pageable, String search) {
        return null;
    }


}

