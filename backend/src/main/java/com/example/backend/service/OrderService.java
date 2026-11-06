package com.example.backend.service;

import java.util.List;

import com.example.backend.dto.OrderRequestDTO;
import com.example.backend.dto.OrderResponseDTO;
import com.example.backend.enums.OrderStatus;

public interface OrderService {

	OrderResponseDTO createOrder(OrderRequestDTO requestDTO);
	OrderResponseDTO getOrderById(Long orderId);	
	
	List<OrderResponseDTO> getMyOrders(String email);
	
	OrderResponseDTO cancelOrder (Long orderId,String email);
	
	OrderResponseDTO  updateOrderStatus(Long orderId,OrderStatus status);
}
