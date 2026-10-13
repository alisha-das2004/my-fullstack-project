package com.example.backend.service;

import java.util.List;

import com.example.backend.dto.ProductRequestDTO;
import com.example.backend.dto.ProductResponseDTO;

public interface ProductService {

    ProductResponseDTO addProduct(ProductRequestDTO request);

    List<ProductResponseDTO> getAllProduct();

    ProductResponseDTO getProductById(Long id);
}
