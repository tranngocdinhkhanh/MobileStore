package khanhtnd.mobilestore.dto.response.cart;

import khanhtnd.mobilestore.dto.response.cart_item.CartItemResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartResponse {
    private int id;
    private BigDecimal totalPrice;
    private List<CartItemResponse> cartItems;
    private String status;
}

