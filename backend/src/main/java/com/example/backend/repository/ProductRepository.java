package com.example.backend.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Product;

public interface ProductRepository  extends JpaRepository<Product, Long>{

	List<Product> findByNameContainingIgnoreCase(String name);
	
	List<Product> findByPriceBetween(BigDecimal minPrice,BigDecimal maxPrice);
	
	List<Product> findAllByOrderByPriceAsc();
	
	List<Product> findAllByOrderByPriceDesc();
}
