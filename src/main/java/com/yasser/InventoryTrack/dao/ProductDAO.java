package com.yasser.InventoryTrack.dao;

import com.yasser.InventoryTrack.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDAO extends JpaRepository<Product, Integer> {}
