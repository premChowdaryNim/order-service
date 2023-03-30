package com.prem.orderservice.dto;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
		@Id
	 	private String id;
	    private String skuCode;
	    private BigDecimal price;
	    private Integer quantity;
}
