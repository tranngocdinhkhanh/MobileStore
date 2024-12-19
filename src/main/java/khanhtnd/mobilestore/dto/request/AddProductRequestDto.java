package khanhtnd.mobilestore.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import khanhtnd.mobilestore.model.Product;
import khanhtnd.mobilestore.utils.Condition;
import lombok.Getter;

import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class AddProductRequestDto {
    @NotNull(message = "Product name cannot be null")
    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 255, message = "Product name cannot exceed 255 characters")
    private String productName;

    @NotNull(message = "Unit price cannot be null")
    @Min(value = 0, message = "Unit price must be greater than or equal to 0")
    private BigDecimal unitPrice;

    @NotNull(message = "Units in stock cannot be null")
    @Min(value = 0, message = "Units in stock must be greater than or equal to 0")
    private int unitInStock;

    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Manufacturer cannot be null")
    @Min(value = 1,message = "Manufacturer's id min is 1")
    private int manufacturer;

    @NotNull(message = "Category cannot be null")
    @Min(value = 1,message = "Category's id min is 1")
    private int category;

    @NotNull(message = "Condition cannot be null")
    @Enumerated(EnumType.STRING)
    private Condition condition;


    public Product toEntity(){
        Product product = new Product();
        product.setProductName(this.productName);
        product.setUnitPrice(this.unitPrice);
        product.setUnitInStock(this.unitInStock);
        product.setDescription(this.description);
        product.setProductCondition(this.condition);
        return product;
    }
}
