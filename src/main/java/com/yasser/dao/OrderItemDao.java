package com.yasser.dao;

import com.yasser.entity.ProductOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDao extends JpaRepository<ProductOrderItem, Integer> {}
