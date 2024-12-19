package khanhtnd.mobilestore.service.impl;


import khanhtnd.mobilestore.dto.response.cart.CartResponse;
import khanhtnd.mobilestore.dto.response.cart_item.CartItemResponse;
import khanhtnd.mobilestore.exception.common.NotFoundException;
import khanhtnd.mobilestore.model.Cart;
import khanhtnd.mobilestore.model.CartItem;
import khanhtnd.mobilestore.model.Product;
import khanhtnd.mobilestore.model.User;
import khanhtnd.mobilestore.repository.CartItemRepository;
import khanhtnd.mobilestore.repository.CartRepository;
import khanhtnd.mobilestore.repository.ProductRepository;
import khanhtnd.mobilestore.repository.UserRepository;
import khanhtnd.mobilestore.service.CartServiceAdvance;
import khanhtnd.mobilestore.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartServiceAdvance {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void add(int userId, int productId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Message.MSG_404, userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Message.MSG_103, productId));
        if (product.getUnitInStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock for product ID: " + productId);
        }

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId() == productId)
                .findFirst()
                .orElse(null);
        if (existingCartItem != null) {

            int totalQuantity = existingCartItem.getQuantity() + quantity;
            if (totalQuantity > product.getUnitInStock()) {
                throw new IllegalArgumentException("Not enough stock for product ID: " + productId
                        + ". Available stock: " + product.getUnitInStock()
                        + ", requested: " + totalQuantity);
            }

            existingCartItem.setQuantity(totalQuantity);
            existingCartItem.setPrice(existingCartItem.getPrice().add(product.getUnitPrice().multiply(BigDecimal.valueOf(quantity))));
            cartItemRepository.save(existingCartItem);
        } else {

            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setPrice(product.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }

        cart.setTotalPrice(cart.getCartItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        cartRepository.save(cart);
    }

    @Override
    public CartResponse viewCart(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Message.MSG_404, userId));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
        List<CartItemResponse> cartItemResponses = cart.getCartItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    return new CartItemResponse(
                            product.getId(),
                            product.getProductName(),
                            product.getDescription(),
                            cartItem.getQuantity(),
                            product.getUnitPrice(),
                            cartItem.getPrice()
                    );
                })
                .collect(Collectors.toList());

        return new CartResponse(
                cart.getId(),
                cart.getTotalPrice(),
                cartItemResponses,
                cart.getStatus().name()
        );
    }

    @Override
    public void removeCartItem(int userId, int productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Message.MSG_404, userId));
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(Message.MSG_404, userId));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(Message.MSG_404, productId));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        // Update cart total price
        cart.setTotalPrice(cart.getCartItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Message.MSG_404, userId));
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(Message.MSG_404, userId));

        // Delete all cart items if cascading is not enabled
        cartItemRepository.deleteAllByCartId(cart.getId());

        // Delete the cart
        cartRepository.deleteByUserId(cart.getId());
    }


}
