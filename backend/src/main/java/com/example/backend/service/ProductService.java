package com.example.backend.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.backend.dto.ProductRequestDTO;
import com.example.backend.dto.ProductResponseDTO;

public interface ProductService {

    ProductResponseDTO addProduct(ProductRequestDTO request);

    List<ProductResponseDTO> getAllProduct();

    ProductResponseDTO getProductById(Long id);
    
    ProductResponseDTO updateProduct(Long id,ProductRequestDTO request);
    
    void deleteProduct(Long id);
    
    List<ProductResponseDTO> searchProduct(String name);
    
    List<ProductResponseDTO> filterProductByPrice(BigDecimal minPrice,BigDecimal maxPrice);
    
    List<ProductResponseDTO>  sortProductsByPrice(String direction);
    
}
