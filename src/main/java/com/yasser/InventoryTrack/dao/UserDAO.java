package com.yasser.InventoryTrack.dao;

import com.yasser.InventoryTrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {
    Integer id(Long id);
}
