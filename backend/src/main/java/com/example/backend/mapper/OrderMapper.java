package com.example.backend.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.backend.dto.OrderItemResponseDTO;
import com.example.backend.dto.OrderResponseDTO;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderItem;

@Component
public class OrderMapper {

	public OrderResponseDTO convertToResponseDTO(Order order) {
		 
		OrderResponseDTO dto = new OrderResponseDTO();
		
		dto.setId(order.getId());
		dto.setUserId(order.getUser().getId());
		dto.setTotalAmount(order.getTotalAmount());
		dto.setStatus(order.getStatus());
		dto.setOrderDate(order.getOrderDate());
		
		List<OrderItemResponseDTO> itemDTOs = new ArrayList<OrderItemResponseDTO>();
		
		for(OrderItem item : order.getItems()) {
			OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
			
			itemDTO.setProductId(item.getProduct().getId());
			itemDTO.setProductName(item.getProduct().getName());
			itemDTO.setQuantity(item.getQuantity());
			itemDTO.setPriceAtPurches(item.getPriceAtPurchase());
			
			BigDecimal subtotal = item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity()));
			itemDTO.setSubtotal(subtotal);
			itemDTOs.add(itemDTO);
			
		}
		dto.setItems(itemDTOs);
		
		return dto;
	}
}
