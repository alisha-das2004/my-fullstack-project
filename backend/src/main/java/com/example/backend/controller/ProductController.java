package com.example.backend.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.ProductRequestDTO;
import com.example.backend.dto.ProductResponseDTO;
import com.example.backend.service.ProductService;

import jakarta.validation.groups.Default;


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
	   @PutMapping("/{id}")
       public ProductResponseDTO updateProduct(@PathVariable Long id,@RequestBody ProductRequestDTO request) {
    	               
    	   ProductResponseDTO response = productService.updateProduct(id, request);
    	   if(response == null) {
    		   throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found with id:"+id);
    		   
    	   }
    	   return response;
       }
	   
	   @DeleteMapping("/{id}")
	   public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		   productService.deleteProduct(id);
		   return ResponseEntity.noContent().build();
	   }
	   
	   @GetMapping("/search")
	   public List<ProductResponseDTO> searchProducts(@RequestParam   String name){
		   
		   return productService.searchProduct(name);
	   }
	   
	   @GetMapping("/filter")
	   public List<ProductResponseDTO>  filterProductByPrice(@RequestParam BigDecimal minPrice,@RequestParam BigDecimal maxPrice){
		   if(minPrice.compareTo(maxPrice)>0 || minPrice.signum()<0) {
			   throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid price range");
		   }
		   return productService.filterProductByPrice(minPrice, maxPrice);
	   }
	   
	   @GetMapping("/sort")
	   public List<ProductResponseDTO> sortProductByPrice(@RequestParam(defaultValue ="asc") String direction){
		   return productService.sortProductsByPrice(direction);
	   }
}
