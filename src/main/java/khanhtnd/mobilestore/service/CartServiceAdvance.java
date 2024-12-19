package khanhtnd.mobilestore.service;

import khanhtnd.mobilestore.dto.response.cart.CartResponse;

public interface CartServiceAdvance {
     void add(int userId, int productId, int quantity) ;
     CartResponse viewCart(int userId);
     void removeCartItem(int userId, int productId);
     void clearCart(int userId);
}
