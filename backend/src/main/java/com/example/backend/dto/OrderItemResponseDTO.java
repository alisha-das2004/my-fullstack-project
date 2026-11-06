package com.example.backend.dto;

import java.math.BigDecimal;

public class OrderItemResponseDTO {

	private Long productId;
	private String productName;
	private Integer Quantity;
	private BigDecimal priceAtPurches;
	private BigDecimal subtotal;
	
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getQuantity() {
		return Quantity;
	}
	public void setQuantity(Integer quantity) {
		Quantity = quantity;
	}
	public BigDecimal getPriceAtPurches() {
		return priceAtPurches;
	}
	public void setPriceAtPurches(BigDecimal priceAtPurches) {
		this.priceAtPurches = priceAtPurches;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	
	
}
