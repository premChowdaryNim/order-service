package com.prem.orderservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prem.orderservice.dto.OrderRequest;
import com.prem.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
    	log.info("Entered ordered service");
        orderService.placeOrder(orderRequest);
        log.info("Exited ordered service");
        return "Order Placed Successfully";
    }
    
    public String fallbackMethod(OrderRequest orderRequest, Exception e) {
    	log.info("ordered service fallback method");
        return  "Oops! Something went wrong, please order after some time!";
    }
}
