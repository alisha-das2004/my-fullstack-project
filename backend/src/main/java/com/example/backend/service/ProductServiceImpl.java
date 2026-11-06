package com.example.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.ProductRequestDTO;
import com.example.backend.dto.ProductResponseDTO;
import com.example.backend.entity.Product;
import com.example.backend.exception.ProductNotFoundException;
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

        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + id));


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
		 
		Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + id));
		
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setStockQuantity(request.getStockQuantity());
		
		Product updateProduct = productRepository.save(product);
		
		return convertToResponseDTO(updateProduct);
	}

	@Override
	public void deleteProduct(Long id) {
		Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + id));
		
		
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

	@Override
	public Page<ProductResponseDTO> getProductsWithPagination(int page, int size) {
		if(page<0 || size<1 || size > 100) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Page must be non-negative and size must be between 1 and 100");
			
		}
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Product> products = productRepository.findAll(pageable);
		
		return products.map(this:: convertToResponseDTO);
	}
	
	
	
	
    
    
}
