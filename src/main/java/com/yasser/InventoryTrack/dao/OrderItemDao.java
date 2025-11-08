package com.yasser.InventoryTrack.dao;

import com.yasser.InventoryTrack.entity.ProductOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDao extends JpaRepository<ProductOrderItem, Integer> {}
