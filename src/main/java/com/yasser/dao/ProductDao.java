package com.yasser.dao;

import com.yasser.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {
    List<Product> findByNameIgnoreCaseContaining(String fragment);
}
