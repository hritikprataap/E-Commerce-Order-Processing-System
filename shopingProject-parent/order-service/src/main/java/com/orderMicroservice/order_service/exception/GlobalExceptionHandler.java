package com.orderMicroservice.order_service.exception;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotInStockException.class)
    public ResponseEntity<Map<String, String>> handleProductNotInStock(ProductNotInStockException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler({InventoryServiceUnavailableException.class, CallNotPermittedException.class})
    public ResponseEntity<Map<String, String>> handleInventoryUnavailable(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("message", "Inventory service is unavailable, please try again later"));
    }
}
