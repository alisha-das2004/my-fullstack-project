package com.example.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name ="orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id",nullable = false)
      private User user;
	
	
      private BigDecimal totalAmount;
      
      @Enumerated(EnumType.STRING)
      private OrderStatus status;
      
      private LocalDateTime orderDate;
      
      @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
      private List<OrderItem> items = new ArrayList();
	  public Order() {
		
	  }
	  public Long getId() {
		  return id;
	  }
	  public void setId(Long id) {
		  this.id = id;
	  }
	  public User getUser() {
		  return user;
	  }
	  public void setUser(User user) {
		  this.user = user;
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
      
	  public List<OrderItem> getItems() {
		    return items;
		}

		public void setItems(List<OrderItem> items) {
		    this.items = items;
		}
      
}
