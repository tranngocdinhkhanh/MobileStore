package khanhtnd.mobilestore.repository;

import khanhtnd.mobilestore.model.Image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByProductId(int productId);
}
