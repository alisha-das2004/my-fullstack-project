package com.example.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.OrderItemRequestDTO;
import com.example.backend.dto.OrderRequestDTO;
import com.example.backend.dto.OrderResponseDTO;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderItem;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.enums.OrderStatus;
import com.example.backend.exception.InsufficientStockException;
import com.example.backend.exception.InvalidOrderException;
import com.example.backend.exception.ProductNotFoundException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.repository.OrderItemRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl  implements OrderService{

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final OrderMapper orderMapper;
	
	
	public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
			ProductRepository productRepository, UserRepository userRepository, OrderMapper orderMapper) {
		super();
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.orderMapper = orderMapper;
	}


	@Override
	@Transactional
	public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
		User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(()-> new UserNotFoundException("User not found with id: " + requestDTO.getUserId()));
		
		Order  order = new Order();
		order.setUser(user);
		order.setStatus(OrderStatus.PENDING);
		order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.ZERO);

		BigDecimal totalAmount = BigDecimal.ZERO;
		
		List<OrderItem> items = new ArrayList<OrderItem>();
		for(OrderItemRequestDTO itemDTO : requestDTO.getItems()) {
			
			if(itemDTO.getQuantity()== null || itemDTO.getQuantity() <= 0) {
				
				throw  new InvalidOrderException("Quantity must be greater than zero");
			}
			
			Product product = productRepository.findById(itemDTO.getProductId()).orElseThrow(()-> new ProductNotFoundException("Product not found: "+ itemDTO.getProductId()));
			if (product.getStockQuantity() < itemDTO.getQuantity()) {

			    throw new InsufficientStockException(
			            "Insufficient stock for product: "
			                    + product.getName());
			}
			 BigDecimal price = BigDecimal.valueOf(product.getPrice());
			  BigDecimal subtotal = price.multiply(
	                    BigDecimal.valueOf(itemDTO.getQuantity()));
             
			  OrderItem item = new OrderItem();
			  item.setOrder(order);
			  item.setProduct(product);
			  item.setQuantity(itemDTO.getQuantity());
			  item.setPriceAtPurchase(price);
			
			  items.add(item);
			  totalAmount = totalAmount.add(subtotal);
			  
			  
			  product.setStockQuantity(product.getStockQuantity()-itemDTO.getQuantity());
			  
			
		}
		order.setItems(items);
		order.setTotalAmount(totalAmount);
		
		Order saveOrder = orderRepository.save(order);
		
		return orderMapper.convertToResponseDTO(saveOrder);
		
	}
     
	@Override
	public OrderResponseDTO getOrderById(Long orderId) {

	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new InvalidOrderException("Order not found with ID: " + orderId));

	    return orderMapper.convertToResponseDTO(order);
	}


	@Override
	public List<OrderResponseDTO> getMyOrders(String email) {
		
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			throw  new UserNotFoundException("User not found");
			
		}
		
		List<Order> orders = orderRepository.findByUserId(user.getId());
		
		List<OrderResponseDTO> responseList =  new ArrayList<OrderResponseDTO>();
		
		for(Order order :orders) {
			responseList.add(orderMapper.convertToResponseDTO(order));
		}
		return responseList;
	}


	@Override
	@Transactional
	public OrderResponseDTO cancelOrder(Long orderId, String email) {
		// TODO Auto-generated method stub
		
		// 1. Find the logged-in user
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			throw new UserNotFoundException("User not found");
		}
		
		// 2. Find the order
		Order order = orderRepository.findById(orderId).orElseThrow(()-> new InvalidOrderException("Order not found"));
		
		// 3. Check whether this order belongs to the logged-in user
		if(!order.getUser().getId().equals(user.getId())) {
			throw  new InvalidOrderException("You are not allowed to cancel this order");
			
		}
		
		// 4. Allow cancellation only for PENDING orders
		if(order.getStatus() != OrderStatus.PENDING) {
			throw new InvalidOrderException(
	                "Only pending orders can be cancelled");
		}
		
		// 5. Restore product stock
		for(OrderItem item : order.getItems()) {
			Product product = item.getProduct();
			
			product.setStockQuantity(product.getStockQuantity()+item.getQuantity());
			productRepository.save(product);
		}
		
		// 6. Update order status
		order.setStatus(OrderStatus.CANCELLED);
		
		// 7. Save the updated order
		Order saveOrder = orderRepository.save(order);
		
		// 8. Convert entity into response DTO
		return orderMapper.convertToResponseDTO(saveOrder);
	}


	@Override
	public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status) {
		
		// 1. Find the order
		Order order = orderRepository.findById(orderId) .orElseThrow(() ->
        new InvalidOrderException("Order not found with ID: " + orderId));
		
		// 2. Check whether the new status is null
		if(status==null) {
			throw new InvalidOrderException("Order status cannot be null");
		}
		
		// 3. Check whether the order is already cancelled
		if(order.getStatus() == OrderStatus.CANCELLED) {
			 throw new InvalidOrderException(
		                "Cancelled orders cannot be updated");
		}
		// 4. Validate the status transition
		if(order.getStatus() == OrderStatus.PENDING && status != OrderStatus.CONFIRMED) {
			 throw new InvalidOrderException(
					 "Pending orders can only be confirmed");
		}
		
		if(order.getStatus() == OrderStatus.CONFIRMED && status != OrderStatus.SHIPPED) {
			throw new InvalidOrderException(
	                "Confirmed orders can only be shipped");
		}
		
		if (order.getStatus() == OrderStatus.SHIPPED
	            && status != OrderStatus.DELIVERED) {
	        throw new InvalidOrderException(
	                "Shipped orders can only be delivered");
	    }

	    if (order.getStatus() == OrderStatus.DELIVERED) {
	        throw new InvalidOrderException(
	                "Delivered orders cannot be updated");
	    }
        
	 // 5. Update the status
	    order.setStatus(status);

	    // 6. Save the updated order
	    Order updatedOrder = orderRepository.save(order);

	    // 7. Convert entity into response DTO
	    return orderMapper.convertToResponseDTO(updatedOrder);
		
	
	}
	
	
	
}
