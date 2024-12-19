package khanhtnd.mobilestore.controller;

import khanhtnd.mobilestore.dto.request.AddProductRequestDto;
import khanhtnd.mobilestore.dto.response.PageDto;
import khanhtnd.mobilestore.dto.response.Response;
import khanhtnd.mobilestore.dto.response.product.ProductResponseDto;
import khanhtnd.mobilestore.service.CommonService;
import khanhtnd.mobilestore.service.ProductServiceAdvance;
import khanhtnd.mobilestore.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {


    private final ProductServiceAdvance productServiceAdvance;
    private final CommonService<ProductResponseDto> commonService;

    @Autowired
    public ProductController(ProductServiceAdvance productServiceAdvance, CommonService<ProductResponseDto> commonService) {

        this.productServiceAdvance = productServiceAdvance;
        this.commonService = commonService;
    }

    @PostMapping
    public ResponseEntity<Response<Integer>> addProduct(@ModelAttribute AddProductRequestDto addProductRequestDto,
                                                        @RequestParam("images") List<MultipartFile> images) {
        int newProductId = productServiceAdvance.addProduct(addProductRequestDto, images);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newProductId)
                .toUri();

        Response<Integer> response = Response.<Integer>builder()
                .code(Message.MSG_201.getCode())
                .description(Message.MSG_201.getDescription())
                .data(newProductId)
                .build();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable int id) {
        ProductResponseDto productResponseDto = commonService.getById(id);
        return ResponseEntity.ok(productResponseDto);
    }

    @GetMapping
    public ResponseEntity<Response<PageDto<ProductResponseDto>>> getAllProductDto(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String search) {
        PageDto<ProductResponseDto> data = commonService.getAll(PageRequest.of(page - 1, size), search);
        Response<PageDto<ProductResponseDto>> response = new Response<>(
                Message.MSG_202.getCode(),
                Message.MSG_202.getDescription(),
                data
        );

        return ResponseEntity.ok(response);
    }
}