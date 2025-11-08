package com.yasser.InventoryTrack.dao;

import com.yasser.InventoryTrack.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<ProductOrder, Integer> {}
