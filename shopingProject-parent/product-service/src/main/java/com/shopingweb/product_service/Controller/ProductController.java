package com.shopingweb.product_service.Controller;

import com.shopingweb.product_service.Service.ProductService;
import com.shopingweb.product_service.dto.ProductRequest;
import com.shopingweb.product_service.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

   // @Autowired
    private final ProductService productService;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void CreateProduct(@RequestBody ProductRequest productRequest){
      productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse>getAllProducts(){
       return productService.getAllProduct();

    }





}
