package khanhtnd.mobilestore.dto.response.cart_item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CartItemResponse {
    private int productId;
    private String productName;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}