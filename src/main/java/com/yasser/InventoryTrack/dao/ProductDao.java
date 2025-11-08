package com.yasser.InventoryTrack.dao;

import com.yasser.InventoryTrack.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {
    List<Product> findByNameIgnoreCaseContaining(String fragment);
}
