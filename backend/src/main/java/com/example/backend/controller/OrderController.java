package com.example.backend.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.OrderRequestDTO;
import com.example.backend.dto.OrderResponseDTO;
import com.example.backend.enums.OrderStatus;
import com.example.backend.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		super();
		this.orderService = orderService;
	}
	
	@PostMapping
	public ResponseEntity<OrderResponseDTO> createOrder( @RequestBody   OrderRequestDTO requestDTO){
		
		
		OrderResponseDTO response = orderService.createOrder(requestDTO);
		
		return new ResponseEntity<OrderResponseDTO>(response,HttpStatus.CREATED);
	}
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponseDTO> getOrderById(
	        @PathVariable Long orderId) {

	    OrderResponseDTO response = orderService.getOrderById(orderId);

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/my-orders")
	public ResponseEntity<List<OrderResponseDTO>> getMyOrders(Authentication authentication){
		String email = authentication.getName();
		List<OrderResponseDTO> orders = orderService.getMyOrders(email);
		
		return ResponseEntity.ok(orders);
	}
	
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long orderId,Authentication authentication){
		  String email = authentication.getName();
		  
		  OrderResponseDTO response = orderService.cancelOrder(orderId, email);
		  
		  return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{orderId}/status")
	public ResponseEntity<OrderResponseDTO> updateOrderStatus(
			@PathVariable  Long orderId,@RequestBody OrderStatus status){
		
		OrderResponseDTO response = orderService.updateOrderStatus(orderId, status);
		
		return ResponseEntity.ok(response);
	}
}
