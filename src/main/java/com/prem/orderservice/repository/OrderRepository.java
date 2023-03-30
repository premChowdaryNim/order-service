package com.prem.orderservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.prem.orderservice.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {

}
