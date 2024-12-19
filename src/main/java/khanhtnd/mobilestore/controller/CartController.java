package khanhtnd.mobilestore.controller;

import khanhtnd.mobilestore.dto.response.Response;
import khanhtnd.mobilestore.dto.response.cart.CartResponse;
import khanhtnd.mobilestore.service.CartServiceAdvance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartServiceAdvance cartServiceAdvance;

    @Autowired
    public CartController(CartServiceAdvance cartServiceAdvance) {
        this.cartServiceAdvance = cartServiceAdvance;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam int userId,
            @RequestParam int productId,
                @RequestParam int quantity) {
        cartServiceAdvance.add(userId, productId, quantity);
        return ResponseEntity.ok("Product added to cart successfully.");
    }

    @GetMapping("/view")
    public ResponseEntity<Response<CartResponse>> viewCart(@RequestParam int userId) {
            CartResponse cartResponse = cartServiceAdvance.viewCart(userId);
            return ResponseEntity.ok(
                    Response.<CartResponse>builder()
                            .code(200)
                            .description("Cart retrieved successfully.")
                            .data(cartResponse)
                            .build()
            );
    }


    @DeleteMapping("/item")
    public ResponseEntity<Response<Void>> removeCartItem(@RequestParam int userId, @RequestParam int productId) {

        cartServiceAdvance.removeCartItem(userId, productId);
        return ResponseEntity.ok(
                Response.<Void>builder()
                        .code(200)
                        .description("Product removed from cart successfully.")
                        .build()
        );
    }
    @DeleteMapping("/clear")
    public ResponseEntity<Response<Void>> clearCart(@RequestParam int userId) {
        cartServiceAdvance.clearCart(userId);
            return ResponseEntity.ok(
                    Response.<Void>builder()
                            .code(200)
                            .description("Cart cleared successfully.")
                            .build()
            );

    }
}
