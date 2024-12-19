package khanhtnd.mobilestore.repository;

import khanhtnd.mobilestore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.cart.id = :cartId")
    void deleteAllByCartId(@Param("cartId") int cartId);
}
