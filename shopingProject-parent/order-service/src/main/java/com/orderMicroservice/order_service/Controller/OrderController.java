package com.orderMicroservice.order_service.Controller;

import com.orderMicroservice.order_service.Service.OrderService;
import com.orderMicroservice.order_service.dto.OrderRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@Transactional
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
    @TimeLimiter(name="inventory")
    @Retry(name="inventory",fallbackMethod = "fallbackMethod")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) throws IllegalAccessException {
     return CompletableFuture.supplyAsync(()-> {
         try {
             return orderService.placeOrder(orderRequest);
         } catch (IllegalAccessException e) {
             throw new RuntimeException(e);
         }
     });

    }
    public CompletableFuture<String>fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(()->"oops something went wrong please order after some time");


        }
    }