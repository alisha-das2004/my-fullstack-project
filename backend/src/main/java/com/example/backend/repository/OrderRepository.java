package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Order;

public interface OrderRepository  extends JpaRepository<Order, Long>{

	List<Order> findByUserId(Long userId);
}
