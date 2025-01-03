package khanhtnd.mobilestore.repository;


import khanhtnd.mobilestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(int id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}