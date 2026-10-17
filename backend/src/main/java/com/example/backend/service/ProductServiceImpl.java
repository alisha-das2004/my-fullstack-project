package com.example.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

	@Override
	public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {
		 
		Product product = productRepository.findById(id).orElse(null);
		if(product == null) {
			return null;
		}
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setStockQuantity(request.getStockQuantity());
		
		Product updateProduct = productRepository.save(product);
		
		return convertToResponseDTO(updateProduct);
	}

	@Override
	public void deleteProduct(Long id) {
		Product product = productRepository.findById(id).orElse(null);
		
		if(product==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product Not Found with Id:"+ id);
			
		}
		productRepository.delete(product);
		
		
	}

	@Override
	public List<ProductResponseDTO> searchProduct(String name) {
		List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
		
		List<ProductResponseDTO> responses = new ArrayList<ProductResponseDTO>();
		for(Product product:products) {
			responses.add(convertToResponseDTO(product));
		}
		return responses;
	}

	@Override
	public List<ProductResponseDTO> filterProductByPrice(BigDecimal minPrice, BigDecimal maxPrice) {
		List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
		
		List<ProductResponseDTO> responses = new ArrayList<ProductResponseDTO>();
		for(Product product : products) {
			responses.add(convertToResponseDTO(product));
		}
		return responses;
	}

	@Override
	public List<ProductResponseDTO> sortProductsByPrice(String direction) {
		List<Product> products;
		if("asc".equalsIgnoreCase(direction)) {
			products = productRepository.findAllByOrderByPriceAsc();
		}else if("desc".equalsIgnoreCase(direction)) {
			products = productRepository.findAllByOrderByPriceDesc();
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Direction must be asc or desc");
		}
		List<ProductResponseDTO> responses = new ArrayList<ProductResponseDTO>();
		for(Product product : products) {
			responses.add(convertToResponseDTO(product));
		}
		return responses;
	}
	
	
    
    
}
