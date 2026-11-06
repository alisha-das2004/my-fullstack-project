package com.example.backend.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequestDTO {
       
	 @JsonProperty("userId")
	private Long userId;
    private List<OrderItemRequestDTO> items;
    
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public List<OrderItemRequestDTO> getItems() {
		return items;
	}
	
	public void setItems(List<OrderItemRequestDTO> items) {
		this.items = items;
	}
    
    
	
}
