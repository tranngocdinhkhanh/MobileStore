package khanhtnd.mobilestore.repository;


import khanhtnd.mobilestore.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturerRepository  extends JpaRepository<Manufacturer, Integer> {

    Optional<Manufacturer> findById(int id);

}
