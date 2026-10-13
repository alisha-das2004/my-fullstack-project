package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.ProductRequestDTO;
import com.example.backend.dto.ProductResponseDTO;
import com.example.backend.service.ProductService;
import com.example.backend.service.ProductServiceImpl;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
	    this.productService = productService;
	}
	   @PostMapping
	   @ResponseStatus(HttpStatus.CREATED)
       public ProductResponseDTO addProduct(@RequestBody ProductRequestDTO request) {
    	   return productService.addProduct(request);
       }
	   @GetMapping
	   public List<ProductResponseDTO>   getAllProduct(){
		   return productService.getAllProduct();
	   }
	   
	   @GetMapping("/{id}")
	   public ProductResponseDTO getProductById( @PathVariable Long id) {
		   ProductResponseDTO response = productService.getProductById(id);
		   if(response == null) {
			   throw new ResponseStatusException(
	                    HttpStatus.NOT_FOUND,
	                    "Product not found with id: " + id);
		   }
		   return response;
	   }
       
}
