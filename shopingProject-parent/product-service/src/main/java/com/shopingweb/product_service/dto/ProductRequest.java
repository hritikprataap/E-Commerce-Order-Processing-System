package com.shopingweb.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//this is being used for crating product
//these are entries a shopkeeper would provide for creating a obj
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
}
