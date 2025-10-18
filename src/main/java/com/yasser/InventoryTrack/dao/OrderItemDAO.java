package com.yasser.InventoryTrack.dao;

import com.yasser.InventoryTrack.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDAO extends JpaRepository<OrderItem, Integer> {}
