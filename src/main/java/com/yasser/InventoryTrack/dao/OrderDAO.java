package com.yasser.InventoryTrack.dao;

import com.yasser.InventoryTrack.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDAO extends JpaRepository<Order, Integer> {}
