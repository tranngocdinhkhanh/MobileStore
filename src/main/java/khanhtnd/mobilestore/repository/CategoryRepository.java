package khanhtnd.mobilestore.repository;

import khanhtnd.mobilestore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository  extends JpaRepository<Category, Integer> {

    Optional<Category> findById(int id);

}