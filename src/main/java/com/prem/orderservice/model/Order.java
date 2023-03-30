package com.prem.orderservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	@Id
	private String id;
    private String orderNumber;
    private List<OrderLineItems> orderLineItemsList;
}
