package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.backend.enums.OrderStatus;

public class OrderResponseDTO {

	private Long id;
	private Long userId;
	private BigDecimal totalAmount;
	private OrderStatus status;
	private LocalDateTime orderDate;
	private List<OrderItemResponseDTO> items;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}
	public List<OrderItemResponseDTO> getItems() {
		return items;
	}
	public void setItems(List<OrderItemResponseDTO> items) {
		this.items = items;
	}
	
}
