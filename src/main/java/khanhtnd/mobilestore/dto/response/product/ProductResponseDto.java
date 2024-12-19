package khanhtnd.mobilestore.dto.response.product;

import khanhtnd.mobilestore.utils.Condition;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductResponseDto {
    private int id;
    private String productName;
    private BigDecimal unitPrice;
    private int unitInStock;
    private String description;
    private String manufacturer;
    private String category;
    private Condition productCondition;
    private List<String> imageUrlList;
}
