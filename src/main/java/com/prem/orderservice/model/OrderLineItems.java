package com.prem.orderservice.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "orderLineItems")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineItems {
	
	@Id
	private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
  
}
