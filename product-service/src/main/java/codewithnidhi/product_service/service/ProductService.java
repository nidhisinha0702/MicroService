package codewithnidhi.product_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import codewithnidhi.product_service.dto.ProductRequest;
import codewithnidhi.product_service.dto.ProductResponse;
import codewithnidhi.product_service.model.Product;
import codewithnidhi.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final ProductRepository productRepository;
	
	public ProductResponse createProduct(ProductRequest productRequest) {
		Product product = Product.builder()
				.name(productRequest.name())
				.description(productRequest.description())
				.price(productRequest.price())
				.build();
		
		productRepository.save(product);
		log.info("Product {} is saved",product.getId());
		return new ProductResponse(product.getId(),product.getName(),product.getDescription(),product.getPrice());
				
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(this::mapToProductResponse).toList();
	}
	
	private ProductResponse mapToProductResponse(Product product) {
		return new ProductResponse(product.getId(),product.getName(),product.getDescription(),product.getPrice());
	}
}
