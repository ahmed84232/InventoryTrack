package com.yasser.dao;

import com.yasser.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<ProductOrder, Integer> {}
