package khanhtnd.mobilestore.service.impl;

import khanhtnd.mobilestore.dto.request.AddProductRequestDto;
import khanhtnd.mobilestore.dto.response.PageDto;
import khanhtnd.mobilestore.dto.response.product.ProductResponseDto;
import khanhtnd.mobilestore.exception.common.InvalidImageException;
import khanhtnd.mobilestore.exception.common.NotFoundException;
import khanhtnd.mobilestore.model.Category;
import khanhtnd.mobilestore.model.Image;
import khanhtnd.mobilestore.model.Manufacturer;
import khanhtnd.mobilestore.model.Product;
import khanhtnd.mobilestore.repository.CategoryRepository;
import khanhtnd.mobilestore.repository.ImageRepository;
import khanhtnd.mobilestore.repository.ManufacturerRepository;
import khanhtnd.mobilestore.repository.ProductRepository;
import khanhtnd.mobilestore.service.CommonService;
import khanhtnd.mobilestore.service.ProductServiceAdvance;
import khanhtnd.mobilestore.utils.Message;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductServiceImpl implements CommonService<ProductResponseDto>, ProductServiceAdvance {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, ImageRepository imageRepository, CategoryRepository categoryRepository, ManufacturerRepository manufacturerRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
        this.categoryRepository = categoryRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    @Transactional
    public int addProduct(AddProductRequestDto productDto, List<MultipartFile> images) {
        // Tạo Product từ DTO
        Category category = categoryRepository.findById(productDto.getCategory())
                .orElseThrow(() -> new NotFoundException(Message.MSG_103, productDto.getCategory()));
        Manufacturer manufacturer = manufacturerRepository.findById(productDto.getManufacturer())
                .orElseThrow(() -> new NotFoundException(Message.MSG_103, productDto.getManufacturer()));
        Product product = productDto.toEntity();
        product.setCategory(category);
        product.setManufacturer(manufacturer);
        // Lưu danh sách hình ảnh vào Product
        List<Image> imageList = new ArrayList<>();
        for (MultipartFile imageFile : images) {
            try {
                // Tạo thực thể Image
                Image image = new Image();
                image.setName(imageFile.getOriginalFilename());
                image.setType(imageFile.getContentType());
                image.setData(imageFile.getBytes());
                image.setProduct(product); // Liên kết ảnh với Product

                imageList.add(image);
            } catch (IOException e) {
                throw new InvalidImageException(Message.MSG_101);
            }
        }
        product.setImages(imageList); // Gán danh sách ảnh vào Product
        // Lưu Product và các Image vào database
        productRepository.save(product);
        return product.getId();
    }

    @Override
    public ProductResponseDto getById(int id) {
        // Tìm sản phẩm trong cơ sở dữ liệu theo ID
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.MSG_103, id));

        // Ánh xạ từ Product sang ProductResponseDto
        ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);

        // Lấy danh sách hình ảnh của sản phẩm từ ImageRepository
        List<Image> images = imageRepository.findByProductId(id);
        List<String> imageUrlList = images.stream()
                .map(image -> "http://localhost:8080/images/" + image.getId()) // Đường dẫn đầy đủ
                .toList(); // Thay thế Collectors.toList()

        // Set danh sách URL hình ảnh vào ProductResponseDto
        productResponseDto.setImageUrlList(imageUrlList);

        return productResponseDto;
    }


    @Override
    public PageDto<ProductResponseDto> getAll(Pageable pageable, String search) {
        var page = productRepository.findProductsByProductNameContainsIgnoreCase(pageable, search);
        PageDto<ProductResponseDto> pageDto = new PageDto<>();

        // Chuyển đổi từng sản phẩm trong danh sách thành ProductResponseDto
        List<ProductResponseDto> productDtoList = page.getContent().stream()
                .map(product -> {
                    // Tạo đối tượng ProductResponseDto
                    var dto = modelMapper.map(product, ProductResponseDto.class);

                    // Ánh xạ danh sách hình ảnh sang URL
                    List<String> imageUrlList = product.getImages().stream()
                            .map(image -> "http://localhost:8080/images/" + image.getId())
                            .toList();

                    dto.setImageUrlList(imageUrlList);
                    return dto;
                })
                .toList();

        // Thiết lập các thông tin vào PageDto
        pageDto.setData(productDtoList);
        pageDto.setPage(pageable.getPageNumber() + 1);
        pageDto.setSize(pageable.getPageSize());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setTotalElements(page.getTotalElements());
        return pageDto;
    }


}
