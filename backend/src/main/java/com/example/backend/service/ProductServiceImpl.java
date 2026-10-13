package com.example.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.ProductRequestDTO;
import com.example.backend.dto.ProductResponseDTO;
import com.example.backend.entity.Product;
import com.example.backend.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        Product savedProduct = productRepository.save(product);

        return convertToResponseDTO(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProduct() {

        List<Product> products = productRepository.findAll();

        List<ProductResponseDTO> responseList = new ArrayList<>();

        for (Product product : products) {
            ProductResponseDTO response = convertToResponseDTO(product);
            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return null;
        }

        return convertToResponseDTO(product);
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {

        ProductResponseDTO response = new ProductResponseDTO();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setActive(product.isActive());

        return response;
    }
}
