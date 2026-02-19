package com.shopingweb.product_service.Service;

import com.shopingweb.product_service.dto.ProductRequest;
import com.shopingweb.product_service.dto.ProductResponse;
import com.shopingweb.product_service.model.Product;
import com.shopingweb.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        //using builder for creating a project
        //we are not using model/entiy for crating project because
        //we want hide some imp imformation
        Product product=Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        //this will create a object of specific product

        //save this to the database
        productRepository.save(product);
        log.info("Product {} is saved",product.getId());

    }

    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.findAll();
        //now i have map this product form product response class
        //saving all the products to a list
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        //going to create a obj using builder method
        //note this is fecting the product obj from model now he is matching the product obj to productResponse obj's fields for showing peoducts
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();

    }
}

